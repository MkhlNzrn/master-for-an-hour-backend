package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Category;
import org.example.exceptions.TaskNotFoundException;
import org.example.pojo.CreateTaskDTO;
import org.example.pojo.TaskDTO;
import org.example.entities.Task;
import org.example.exceptions.NoTasksFoundException;
import org.example.pojo.UpdateTaskDTO;
import org.example.repositories.CategoryRepository;
import org.example.repositories.TaskRepository;
import org.example.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final CategoryRepository categoryRepository;

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
        Task task = new Task(taskDTO.getName(), category);
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
        if (!taskDTO.getName().isEmpty() && !taskDTO.getName().equals(task.getName())) task.setName(taskDTO.getName());
        taskRepository.save(task);
        return task.getId();
    }

    private TaskDTO convertToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .name(task.getName())
                .categoryId(task.getCategory().getId())
                .categoryName(task.getCategory().getName())
                .build();
    }
}
