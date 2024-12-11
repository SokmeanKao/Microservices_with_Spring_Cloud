package org.example.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.model.enumeration.SortDirection;
import org.example.model.request.TaskRequest;
import org.example.model.response.ApiResponse;
import org.example.model.response.TaskResponse;
import org.example.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/task")
@CrossOrigin(origins = "http://localhost:9090")
@SecurityRequirement(name = "keycloak_auth")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest) {
        ApiResponse<TaskResponse> response = ApiResponse.<TaskResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Create task successfully!")
                .payload(taskService.createTask(taskRequest))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable String taskId) {
        ApiResponse<TaskResponse> response = ApiResponse.<TaskResponse>builder()
                .status(HttpStatus.OK)
                .message("Get task by ID successfully!")
                .payload(taskService.getTaskById(taskId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTask(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "taskId") String sortField,
            @RequestParam(defaultValue = "ASC") SortDirection sortDirection // Default value added
    ) {
        ApiResponse<List<TaskResponse>> response = ApiResponse.<List<TaskResponse>>builder()
                .status(HttpStatus.OK)
                .message("Get all tasks successfully!")
                .payload(taskService.getAllTask(page, size, sortField, sortDirection))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTaskById(@PathVariable String taskId) {
        taskService.deleteTaskById(taskId);
        ApiResponse<?> response = ApiResponse.<TaskResponse>builder()
                .status(HttpStatus.OK)
                .message("Deleted task by ID successfully!")
                .payload(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTaskById(@RequestBody TaskRequest taskRequest,@PathVariable String taskId) {
        ApiResponse<TaskResponse> response = ApiResponse.<TaskResponse>builder()
                .status(HttpStatus.OK)
                .message("Updated task by ID successfully!")
                .payload(taskService.updateTaskById(taskRequest,taskId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
