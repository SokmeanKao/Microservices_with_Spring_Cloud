package org.example.model.mapper;

import org.example.model.User;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

public class UserMapper {

    public static User toDto(UserRepresentation userRepresentation) {
        User user = new User();
        user.setId(UUID.fromString(userRepresentation.getId()));
        user.setUsername(userRepresentation.getUsername());
        user.setEmail(userRepresentation.getEmail());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());

        // Use createdTimestamp instead of custom attribute
        if (userRepresentation.getCreatedTimestamp() != null) {
            user.setCreatedDate(Instant.ofEpochMilli(userRepresentation.getCreatedTimestamp())
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        // Handle lastModified if stored in attributes
        if (userRepresentation.getAttributes() != null) {
            List<String> lastModifiedList = userRepresentation.getAttributes().get("lastModified");
            if (lastModifiedList != null && !lastModifiedList.isEmpty()) {
                user.setLastModified(LocalDateTime.parse(lastModifiedList.getFirst()));
            }
        }

        return user;
    }
}

