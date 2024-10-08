package org.example.services;

import org.example.entities.Task;
import org.example.pojo.CreateTaskDTO;
import org.example.pojo.SendFeedbackDTO;
import org.example.pojo.TaskDTO;
import org.example.pojo.UpdateTaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    Page<TaskDTO> getTasks(Pageable pageable);

    TaskDTO getTask(Long id);

    Long createTask(CreateTaskDTO taskDTO);

    Long deleteTask(Long id);

    Long updateTask(UpdateTaskDTO taskDTO);

    List<TaskDTO> getTasksByCategoryId(Long id);

    List<TaskDTO> getTasksByClientsUserId(Long id);

    List<TaskDTO> getAllTasksByMastersUserId(Long id);

    Long markAsCompleted(Long id);

    Long sendFeedback(SendFeedbackDTO sendFeedbackDTO);

    List<TaskDTO> getAllTasksByCategoryIds(List<Long> ids);
}
