package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.response.TaskResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "task_tb")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID taskId;
    private String taskName;
    private String description;
    private String createBy;
    private String assignedTo;
    private String groupId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;

    public TaskResponse toResponse(){
        return new TaskResponse(this.taskId,this.taskName,this.description,null,null,null,this.createdDate,this.lastModified);
    }
}
