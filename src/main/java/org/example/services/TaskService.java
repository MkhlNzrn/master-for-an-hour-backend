package org.example.services;

import org.example.pojo.CreateTaskDTO;
import org.example.pojo.TaskDTO;
import org.example.pojo.UpdateTaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {
    Page<TaskDTO> getTasks(Pageable pageable);

    TaskDTO getTask(Long id);

    Long createTask(CreateTaskDTO taskDTO);

    Long deleteTask(Long id);

    Long updateTask(UpdateTaskDTO taskDTO);
}
