package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.*;
import org.example.exceptions.*;
import org.example.pojo.CreateTaskDTO;
import org.example.pojo.SendFeedbackDTO;
import org.example.pojo.TaskDTO;
import org.example.pojo.UpdateTaskDTO;
import org.example.repositories.*;
import org.example.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final MasterRepository masterRepository;

    @Override
    public Page<TaskDTO> getTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAllUncompletedTasksPage(pageable);
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
                        .orElseThrow(() -> new UsernameNotFoundException("User not found by ID: " + taskDTO.getUserId())),
                taskDTO.getMaxPrice());
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
        if (taskDTO.getIsCompleted() != null && !taskDTO.getIsCompleted().equals(task.getIsCompleted()))
            task.setIsCompleted(taskDTO.getIsCompleted());
        if (!taskDTO.getFeedback().isEmpty() && !taskDTO.getFeedback().equals(task.getFeedback()))
            task.setFeedback(taskDTO.getFeedback());
        if (taskDTO.getRate() != null && !taskDTO.getRate().equals(task.getRate()))
            task.setRate(taskDTO.getRate());
        if (taskDTO.getMaxPrice() != null && !taskDTO.getMaxPrice().equals(task.getPrice()))
            task.setMaxPrice(taskDTO.getMaxPrice());
        taskRepository.save(task);
        return task.getId();
    }

    @Override
    public List<TaskDTO> getTasksByCategoryId(Long id) {
        List<Task> tasks = taskRepository.findAllByCategory(categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id)));
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

    @Override
    public List<TaskDTO> getTasksByClientsUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by Id: " + id));
        List<Task> tasks = taskRepository.findAllByClientsUserId(user);
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(this::convertToDTO)
                .toList();

        taskDTOs.forEach(taskDTO -> taskDTO.setUserName(user.getFirstName()));
        return taskDTOs;
    }

    public Long sendFeedback(SendFeedbackDTO sendFeedbackDTO) {
        Task task = taskRepository.findById(sendFeedbackDTO.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException(sendFeedbackDTO.getTaskId()));
        Master master = masterRepository.findByUser(task.getMaster())
                .orElseThrow(() -> new MasterNotFoundException(task.getMaster().getUsername()));
        task.setFeedback(sendFeedbackDTO.getFeedback());
        task.setRate(sendFeedbackDTO.getRate());
        if (master.getRate() != 0) master.setRate((master.getRate()+sendFeedbackDTO.getRate())/2);
        else master.setRate(sendFeedbackDTO.getRate());
        masterRepository.save(master);
        return taskRepository.save(task).getId();
    }

    @Override
    public List<TaskDTO> getAllTasksByCategoryIds(List<Long> ids) {
        return taskRepository.findAllByCategoryIdsIn(ids).stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<TaskDTO> getAllTasksByMastersUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by Id: " + id));
        return taskRepository.findAllByMastersUserId(user).stream().map(this::convertToDTO).toList();
    }

    @Override
    public Long markAsCompleted(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setIsCompleted(true);
        taskRepository.save(task);
        return task.getId();
    }


    private TaskDTO convertToDTO(Task task) {
        User user = task.getMaster();
        Long userId;
        if (user == null) userId  = null;
        else userId = user.getId();
        Master master;
        if (masterRepository.findByUser(user).isEmpty()) master = new Master(null,null,null);
        else master = masterRepository.findByUser(user).get();
        Client client = clientRepository.findByEmail(task.getClient().getUsername())
                .orElseThrow(() -> new ClientNotFoundException(task.getClient().getUsername()));
        return TaskDTO.builder()
                .id(task.getId())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .rate(task.getRate())
                .feedback(task.getFeedback())
                .isCompleted(task.getIsCompleted())
                .categoryId(task.getCategory().getId())
                .categoryName(task.getCategory().getName())
                .userId(task.getClient().getId())
                .userName(task.getClient().getFirstName())
                .maxPrice(task.getMaxPrice())
                .clientEmail(client.getEmail())
                .clientPhoneNumber(client.getPhoneNumber())
                .clientTelegramTag(client.getTelegramTag())
                .masterId(userId)
                .masterName(master.getMiddleName()+" "+master.getFirstName()+" "+master.getLastName())
                .masterEmail(master.getEmail())
                .masterPhoneNumber(master.getPhoneNumber())
                .masterTelegramTag(master.getTelegramTag())
                .build();
    }
}
