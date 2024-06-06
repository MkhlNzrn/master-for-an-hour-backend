package org.example.services;

import org.example.pojo.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {
    Page<TaskDTO> getTasks(Pageable pageable);
}
