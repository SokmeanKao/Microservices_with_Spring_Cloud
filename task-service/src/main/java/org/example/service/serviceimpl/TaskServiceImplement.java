package org.example.service.serviceimpl;

import org.example.client.GroupServiceClient;
import org.example.client.UserServiceClient;
import org.example.model.Task;
import org.example.model.enumeration.SortDirection;
import org.example.model.request.TaskRequest;
import org.example.model.response.GroupResponse;
import org.example.model.response.TaskResponse;
import org.example.model.response.UserResponse;
import org.example.repository.TaskRepository;
import org.example.service.TaskService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class TaskServiceImplement implements TaskService {
    private final TaskRepository taskRepository;
    private final UserServiceClient userServiceClient;
    private final GroupServiceClient groupServiceClient;

    public TaskServiceImplement(TaskRepository taskRepository, UserServiceClient userServiceClient, GroupServiceClient groupServiceClient) {
        this.taskRepository = taskRepository;
        this.userServiceClient = userServiceClient;
        this.groupServiceClient = groupServiceClient;
    }

    @Override
    @CircuitBreaker(name = "createTaskCircuitBreaker", fallbackMethod = "createTaskFallback")
    public TaskResponse createTask(TaskRequest taskRequest) {
        Task task = taskRepository.save(taskRequest.toEntity());

        // Fetch user and group details using userServiceClient and groupServiceClient
        UserResponse createBy = Objects.requireNonNull(userServiceClient.getUserById(taskRequest.getCreateBy()).getBody()).getPayload();
        UserResponse assignedTo = Objects.requireNonNull(userServiceClient.getUserById(taskRequest.getAssignedTo()).getBody()).getPayload();
        GroupResponse group = Objects.requireNonNull(groupServiceClient.getGroupById(taskRequest.getGroupId()).getBody()).getPayload();

        return new TaskResponse(
                task.getTaskId(),
                task.getTaskName(),
                task.getDescription(),
                group,
                createBy,
                assignedTo,
                task.getCreatedDate(),
                task.getLastModified()
        );
    }

    public TaskResponse createTaskFallback(TaskRequest taskRequest, Throwable throwable) {
        Task task = taskRepository.save(taskRequest.toEntity());

        TaskResponse taskResponse = new TaskResponse(
                task.getTaskId(),
                task.getTaskName(),
                task.getDescription(),
                new GroupResponse("unknown-group-id", "Unknown Group"),
                new UserResponse("unknown-createBy-id", "Unknown User", "", "", ""),
                new UserResponse("unknown-assignedTo-id", "Unknown User", "", "", ""),
                task.getCreatedDate(),
                task.getLastModified()
        );

        taskResponse.setDescription("Fallback: Could not fetch user or group data. Reason: " + throwable.getMessage());
        return taskResponse;
    }

    @Override
    @CircuitBreaker(name = "getTaskByIdCircuitBreaker", fallbackMethod = "getTaskByIdFallback")
    public TaskResponse getTaskById(String taskId) {
        Task task = taskRepository.findById(UUID.fromString(taskId)).orElseThrow();

        UserResponse createBy = Objects.requireNonNull(userServiceClient.getUserById(task.getCreateBy()).getBody()).getPayload();
        UserResponse assignedTo = Objects.requireNonNull(userServiceClient.getUserById(task.getAssignedTo()).getBody()).getPayload();
        GroupResponse group = Objects.requireNonNull(groupServiceClient.getGroupById(task.getGroupId()).getBody()).getPayload();

        return new TaskResponse(
                task.getTaskId(),
                task.getTaskName(),
                task.getDescription(),
                group,
                createBy,
                assignedTo,
                task.getCreatedDate(),
                task.getLastModified()
        );
    }

    public TaskResponse getTaskByIdFallback(String taskId, Throwable throwable) {
        Task task = taskRepository.findById(UUID.fromString(taskId)).orElseThrow();

        return new TaskResponse(
                task.getTaskId(),
                task.getTaskName(),
                task.getDescription(),
                new GroupResponse("unknown-group-id", "Unknown Group"),
                new UserResponse("unknown-createBy-id", "Unknown User", "", "", ""),
                new UserResponse("unknown-assignedTo-id", "Unknown User", "", "", ""),
                task.getCreatedDate(),
                task.getLastModified()
        );
    }

    @Override
    @CircuitBreaker(name = "getAllTasksCircuitBreaker", fallbackMethod = "getAllTasksFallback")
    public List<TaskResponse> getAllTask(Integer page, Integer size, String sortField, SortDirection sortDirection) {
        Sort sort = sortDirection.name().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        return taskRepository.findAll(pageable).stream().map(task -> {
            UserResponse createBy = Objects.requireNonNull(userServiceClient.getUserById(task.getCreateBy()).getBody()).getPayload();
            UserResponse assignedTo = Objects.requireNonNull(userServiceClient.getUserById(task.getAssignedTo()).getBody()).getPayload();
            GroupResponse group = Objects.requireNonNull(groupServiceClient.getGroupById(task.getGroupId()).getBody()).getPayload();

            return new TaskResponse(
                    task.getTaskId(),
                    task.getTaskName(),
                    task.getDescription(),
                    group,
                    createBy,
                    assignedTo,
                    task.getCreatedDate(),
                    task.getLastModified()
            );
        }).toList();
    }

    public List<TaskResponse> getAllTasksFallback(Integer page, Integer size, String sortField, SortDirection sortDirection, Throwable throwable) {
        return List.of(new TaskResponse(
                UUID.randomUUID(),
                "Fallback Task",
                "Fallback description due to: " + throwable.getMessage(),
                new GroupResponse("unknown-group-id", "Unknown Group"),
                new UserResponse("unknown-createBy-id", "Unknown User", "", "", ""),
                new UserResponse("unknown-assignedTo-id", "Unknown User", "", "", ""),
                LocalDateTime.now(),
                LocalDateTime.now()
        ));
    }

    @Override
    @CircuitBreaker(name = "updateTaskByIdCircuitBreaker", fallbackMethod = "updateTaskByIdFallback")
    public TaskResponse updateTaskById(TaskRequest taskRequest, String taskId) {
        Task task = taskRepository.findById(UUID.fromString(taskId)).orElseThrow();

        task.setTaskName(taskRequest.getTaskName());
        task.setDescription(taskRequest.getDescription());
        task.setAssignedTo(taskRequest.getAssignedTo());
        task.setCreateBy(taskRequest.getCreateBy());
        task.setGroupId(taskRequest.getGroupId());
        task.setLastModified(LocalDateTime.now());
        taskRepository.save(task);

        UserResponse createBy = Objects.requireNonNull(userServiceClient.getUserById(task.getCreateBy()).getBody()).getPayload();
        UserResponse assignedTo = Objects.requireNonNull(userServiceClient.getUserById(task.getAssignedTo()).getBody()).getPayload();
        GroupResponse group = Objects.requireNonNull(groupServiceClient.getGroupById(task.getGroupId()).getBody()).getPayload();

        return new TaskResponse(
                task.getTaskId(),
                task.getTaskName(),
                task.getDescription(),
                group,
                createBy,
                assignedTo,
                task.getCreatedDate(),
                task.getLastModified()
        );
    }

    public TaskResponse updateTaskByIdFallback(TaskRequest taskRequest, String taskId, Throwable throwable) {
        Task task = taskRepository.findById(UUID.fromString(taskId)).orElseThrow();

        return new TaskResponse(
                task.getTaskId(),
                task.getTaskName(),
                "Fallback: Could not update the task. Reason: " + throwable.getMessage(),
                new GroupResponse("unknown-group-id", "Unknown Group"),
                new UserResponse("unknown-createBy-id", "Unknown User", "", "", ""),
                new UserResponse("unknown-assignedTo-id", "Unknown User", "", "", ""),
                task.getCreatedDate(),
                LocalDateTime.now()
        );
    }

    @Override
    @CircuitBreaker(name = "deleteTaskByIdCircuitBreaker", fallbackMethod = "deleteTaskByIdFallback")
    public void deleteTaskById(String taskId) {
        taskRepository.deleteById(UUID.fromString(taskId));
    }

    public void deleteTaskByIdFallback(String taskId, Throwable throwable) {
        throw new RuntimeException("Fallback: Could not delete task. Reason: " + throwable.getMessage());
    }
}

