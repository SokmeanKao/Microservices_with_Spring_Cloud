package org.example.service;

import org.example.model.enumeration.SortDirection;
import org.example.model.request.TaskRequest;
import org.example.model.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest taskRequest);
    TaskResponse getTaskById(String taskId);
    List<TaskResponse> getAllTask(Integer page, Integer size, String sortField, SortDirection sortDirection);
    void deleteTaskById(String taskId);
    TaskResponse updateTaskById(TaskRequest taskRequest, String taskId);
}
