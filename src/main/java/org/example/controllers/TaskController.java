package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pojo.CreateTaskDTO;
import org.example.pojo.TaskDTO;
import org.example.pojo.UpdateTaskDTO;
import org.example.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Operations with Tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(description = "Get an information about all Tasks")
    @GetMapping("/")
    public ResponseEntity<Page<TaskDTO>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getTasks(pageRequest));
    }

    @Operation(description = "Get an information about Task")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @Operation(description = "Create new Task")
    @PostMapping("/")
    public ResponseEntity<Long> createTask(@RequestBody CreateTaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.createTask(taskDTO));
    }

    @Operation(description = "Delete Task")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

    @Operation(description = "Update Task")
    @PatchMapping("/")
    public ResponseEntity<Long> updateTask(@RequestBody UpdateTaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(taskDTO));
    }
}
