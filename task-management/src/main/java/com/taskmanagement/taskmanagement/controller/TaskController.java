package com.taskmanagement.taskmanagement.controller;

import com.taskmanagement.taskmanagement.dto.TaskRequest;
import com.taskmanagement.taskmanagement.dto.TaskResponse;
import com.taskmanagement.taskmanagement.entity.TaskPriority;
import com.taskmanagement.taskmanagement.entity.TaskStatus;
import com.taskmanagement.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {

        String userEmail = authentication.getName();

        TaskResponse response =
                taskService.createTask(request, userEmail);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
 @GetMapping
public ResponseEntity<List<TaskResponse>> getAllTasks(
        @RequestParam(required = false) TaskStatus status,
        @RequestParam(required = false) TaskPriority priority,
        @RequestParam(required = false) Long assignedTo,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String direction) {

    List<TaskResponse> tasks =
            taskService.getAllTasks(
                    status,
                    priority,
                    assignedTo,
                    search,
                    sortBy,
                    direction
            );

    return ResponseEntity.ok(tasks);
}
@GetMapping("/{taskId}")
public ResponseEntity<TaskResponse> getTaskById(
        @PathVariable Long taskId) {

    TaskResponse response =
            taskService.getTaskById(taskId);

    return ResponseEntity.ok(response);
}
@PutMapping("/{taskId}")
public ResponseEntity<TaskResponse> updateTask(
        @PathVariable Long taskId,
        @Valid @RequestBody TaskRequest request) {

    TaskResponse response =
            taskService.updateTask(taskId, request);

    return ResponseEntity.ok(response);
}
@DeleteMapping("/{taskId}")
public ResponseEntity<Void> deleteTask(
        @PathVariable Long taskId) {

    taskService.deleteTask(taskId);

    return ResponseEntity.noContent().build();
}
@PutMapping("/{taskId}/assign/{userId}")
public ResponseEntity<TaskResponse> assignTask(
        @PathVariable Long taskId,
        @PathVariable Long userId) {

    TaskResponse response =
            taskService.assignTask(taskId, userId);

    return ResponseEntity.ok(response);
}
@PatchMapping("/{taskId}/status")
public ResponseEntity<TaskResponse> updateTaskStatus(
        @PathVariable Long taskId,
        @RequestParam TaskStatus status) {

    TaskResponse response =
            taskService.updateTaskStatus(taskId, status);

    return ResponseEntity.ok(response);
}
@PatchMapping("/{taskId}/priority")
public ResponseEntity<TaskResponse> updateTaskPriority(
        @PathVariable Long taskId,
        @RequestParam TaskPriority priority) {

    TaskResponse response =
            taskService.updateTaskPriority(taskId, priority);

    return ResponseEntity.ok(response);
}
}