package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.example.pojo.TaskDTO;
import org.example.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get an information about all Tasks")
    @GetMapping("/")
    public ResponseEntity<Page<TaskDTO>> getAllMasters(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getTasks(pageRequest));
    }

}
