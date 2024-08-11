package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.entities.Category;
import org.example.entities.Task;
import org.example.exceptions.NoCategoriesFoundException;
import org.example.pojo.CreateTaskDTO;
import org.example.pojo.SendFeedbackDTO;
import org.example.pojo.TaskDTO;
import org.example.pojo.UpdateTaskDTO;
import org.example.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(description = "Get an information about all Tasks by Master's UserId")
    @GetMapping("/master/{id}")
    public ResponseEntity<List<TaskDTO>> getAllTasksByMastersUserId(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getAllTasksByMastersUserId(id));
    }

    @Operation(description = "Search tasks by Category")
    @GetMapping("/category/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksByCategoryId(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTasksByCategoryId(id));
    }

    @Operation(description = "Get Tasks by CategoryIds")
    @GetMapping("/category/list")
    public ResponseEntity<List<TaskDTO>> getCategoryByIds(@RequestParam List<Long> categoryIds) {
        return ResponseEntity.ok(taskService.getAllTasksByCategoryIds(categoryIds));
    }

    @Operation(description = "Search tasks by Client's UserId")
    @GetMapping("/client/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksByClientsUserId(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTasksByClientsUserId(id));
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

    @Operation(description = "Send feedback")
    @PatchMapping("/feedback")
    public ResponseEntity<Long> sendFeedback(@RequestBody SendFeedbackDTO sendFeedbackDTO) {
        return ResponseEntity.ok(taskService.sendFeedback(sendFeedbackDTO));
    }

    @Operation(description = "Mark Task as completed")
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Long> markAsCompleted(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.markAsCompleted(id));
    }
}
