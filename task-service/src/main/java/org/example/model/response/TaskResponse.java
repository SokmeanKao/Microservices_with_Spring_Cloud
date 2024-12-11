package org.example.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private UUID taskId;
    private String taskName;
    private String description;
    private GroupResponse group;
    private UserResponse createBy;
    private UserResponse assignedTo;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;
}
