package org.example.model;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Group {
    private UUID groupId;
    private String groupName;
}
