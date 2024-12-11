package org.example.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.Task;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

    @Schema(description = "Name of the task", example = "Backend")
    private String taskName;

    @Schema(description = "Task description", example = "Implement the new feature for the application")
    private String description;

    @Schema(description = "UUID of the user who created the task", example = "d3b07384-d9b0-12d4-a716-446655440000")
    private String createBy;

    @Schema(description = "UUID of the user to whom the task is assigned", example = "9f7a9c84-90b4-45b7-9f0d-45b9335d14f1")
    private String assignedTo;

    @Schema(description = "UUID of the group this task belongs to", example = "b67ebbb3-47d2-49b8-965d-32e0b554f5f2")
    private String groupId;

    public Task toEntity() {
        return new Task(
                null,
                this.taskName,
                this.description,
                this.createBy,
                this.assignedTo,
                this.groupId,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
