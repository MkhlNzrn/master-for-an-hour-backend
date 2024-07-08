package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Category;
import org.example.entities.User;
import org.example.exceptions.CategoryNotFoundException;
import org.example.exceptions.TaskNotFoundException;
import org.example.pojo.CreateTaskDTO;
import org.example.pojo.TaskDTO;
import org.example.entities.Task;
import org.example.exceptions.NoTasksFoundException;
import org.example.pojo.UpdateTaskDTO;
import org.example.repositories.CategoryRepository;
import org.example.repositories.TaskRepository;
import org.example.repositories.UserRepository;
import org.example.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public Page<TaskDTO> getTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAllTasksPage(pageable);
        if (tasks.isEmpty()) throw new NoTasksFoundException();
        return tasks.map(this::convertToDTO);
    }

    @Override
    public TaskDTO getTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return convertToDTO(task);
    }

    @Override
    public Long createTask(CreateTaskDTO taskDTO) {
        Optional<Category> categoryOp = categoryRepository.findByName(taskDTO.getCategoryName());
        Category category = categoryOp.orElseGet(() -> new Category(taskDTO.getCategoryName()));
        categoryRepository.save(category);
        Task task = new Task(taskDTO.getDescription(),
                category, taskDTO.getStartDate(),
                taskDTO.getEndDate(),
                userRepository.findById(taskDTO.getUserId())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found by ID: " + taskDTO.getUserId())));
        return taskRepository.save(task).getId();
    }

    @Override
    public Long deleteTask(Long id) {
        taskRepository.deleteById(id);
        return id;
    }

    @Override
    public Long updateTask(UpdateTaskDTO taskDTO) {
        Task task = taskRepository.findById(taskDTO.getId()).orElseThrow(() -> new TaskNotFoundException(taskDTO.getId()));
        if (!taskDTO.getCategoryName().isEmpty() && !taskDTO.getCategoryName().equals(task.getCategory().getName())) {
            Category category = categoryRepository.findByName(taskDTO.getCategoryName())
                    .orElseGet(() -> new Category(taskDTO.getCategoryName()));
            task.setCategory(category);
        }
        if (!taskDTO.getDescription().isEmpty() && !taskDTO.getDescription().equals(task.getDescription()))
            task.setDescription(taskDTO.getDescription());
        if (!taskDTO.getStartDate().toString().isEmpty() && !taskDTO.getStartDate().equals(task.getStartDate()))
            task.setStartDate(taskDTO.getStartDate());
        if (!taskDTO.getEndDate().toString().isEmpty() && !taskDTO.getEndDate().equals(task.getEndDate()))
            task.setEndDate(taskDTO.getEndDate());
        taskRepository.save(task);
        return task.getId();
    }

    @Override
    public List<TaskDTO> getTasksByCategoryId(Long id) {
        List<Task> tasks = taskRepository.findAllByCategory(categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id)));
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(this::convertToDTO)
                .toList();

        taskDTOs.forEach(taskDTO -> {
            User user = userRepository.findById(taskDTO.getUserId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found by Id: " + taskDTO.getUserId()));
            taskDTO.setUserName(user.getFirstName());
        });

        return taskDTOs;
    }

    private TaskDTO convertToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .categoryId(task.getCategory().getId())
                .categoryName(task.getCategory().getName())
                .userId(task.getUser().getId())
                .build();
    }
}
