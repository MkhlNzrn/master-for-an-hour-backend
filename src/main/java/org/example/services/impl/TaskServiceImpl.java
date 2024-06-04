package org.example.services.impl;

import org.example.dtos.TaskDTO;
import org.example.entites.Task;
import org.example.exceptions.NoTasksFoundException;
import org.example.repositories.TaskRepository;
import org.example.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Page<TaskDTO> getTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAllTasksPage(pageable);
        if (tasks.isEmpty()) throw new NoTasksFoundException();
        return tasks.map(this::convertToDTO);
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
