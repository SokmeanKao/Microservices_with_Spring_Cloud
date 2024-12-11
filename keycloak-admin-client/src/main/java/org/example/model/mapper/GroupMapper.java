package org.example.model.mapper;

import org.example.model.Group;
import org.example.model.User;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

public class GroupMapper {
    public static Group toDto(GroupRepresentation groupRepresentation) {
        Group group = new Group();
        group.setGroupId(UUID.fromString(groupRepresentation.getId()));
        group.setGroupName(groupRepresentation.getName());
        return group;
    }
}