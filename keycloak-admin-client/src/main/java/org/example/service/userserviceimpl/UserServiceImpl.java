package org.example.service.userserviceimpl;

import jakarta.ws.rs.core.Response;
import org.example.exception.AlreadyExistException;
import org.example.exception.NotFoundException;
import org.example.model.User;
import org.example.model.mapper.UserMapper;
import org.example.model.request.UserRequest;
import org.example.model.request.UserUpdateRequest;
import org.example.service.UserService;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public UserServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public List<User> getAllUsers() {
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
        return userRepresentations.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(UserRequest userRequest) {
        // Prepare UserRepresentation with necessary fields
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRequest.getUsername());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.singleAttribute("lastModified",String.valueOf(LocalDateTime.now()));

        user.setEnabled(true);  // Enable the user

        // Set password
        CredentialRepresentation passwordCredential = createPasswordCredentials(userRequest.getPassword());
        user.setCredentials(Collections.singletonList(passwordCredential));

        // Create the user in Keycloak
        UsersResource usersResource = keycloak.realm(realm).users();
        Response response = usersResource.create(user);

        // Handle response
        if (response.getStatus() == 201) {
            // Fetch the newly created user by ID
            String userId = CreatedResponseUtil.getCreatedId(response);
            UserRepresentation createdUser = usersResource.get(userId).toRepresentation();

            if (createdUser != null) {
                // Map and return as custom DTO
                return UserMapper.toDto(createdUser);
            } else {
                throw new RuntimeException("User created, but fetching failed.");
            }
        } else if (response.getStatus() == 409) {
            throw new AlreadyExistException("User already exists.");
        } else {
            throw new RuntimeException("Failed to create user: " + response.getStatusInfo());
        }
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);  // Make the password non-temporary
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        return credential;
    }

    @Override
    public User getUserById(UUID id) {
        try {
            UserRepresentation userRepresentation = keycloak.realm(realm).users().get(id.toString()).toRepresentation();
            return UserMapper.toDto(userRepresentation);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user by ID", e);
        }
    }

    @Override
    public User updateUser(UUID id, UserUpdateRequest userUpdateRequest) {
        UsersResource usersResource = keycloak.realm(realm).users();

        try {
            // Fetch the existing user
            UserRepresentation existingUser = usersResource.get(id.toString()).toRepresentation();
            if (existingUser == null) {
                throw new NotFoundException("User not found");
            }

            // Update user details based on the userUpdateRequest object
            existingUser.setFirstName(userUpdateRequest.getFirstName());
            existingUser.setLastName(userUpdateRequest.getLastName());
            existingUser.setEmail(userUpdateRequest.getEmail());
            existingUser.singleAttribute("lastModified", String.valueOf(LocalDateTime.now()));
            // Update the user in Keycloak
            usersResource.get(id.toString()).update(existingUser);

            // Fetch the updated user and map to DTO
            UserRepresentation updatedUser = usersResource.get(id.toString()).toRepresentation();
            return UserMapper.toDto(updatedUser);  // Assuming UserMapper is implemented
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public String deleteUser(UUID id) {
        UsersResource usersResource = keycloak.realm(realm).users();

        try {
            // Check if the user exists before deleting
            UserRepresentation user = usersResource.get(id.toString()).toRepresentation();
            if (user == null) {
                throw new NotFoundException("User not found");
            }

            // Delete the user
            usersResource.delete(id.toString());
            return "User successfully deleted";
        } catch (NotFoundException e) {
            throw e;  // Rethrow if user not found
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    // Get users by username
    @Override
    public List<User> getUserByUsername(String username) {
        try {
            // Fetch user(s) from Keycloak based on the username
            List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().searchByUsername(username, true);

            // Map Keycloak UserRepresentation to your custom User DTO
            return userRepresentations.stream()
                    .map(UserMapper::toDto)  // Assuming UserMapper is implemented
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user by username: " + username, e);
        }
    }

    // Get users by email
    @Override
    public List<User> getUserByEmail(String email) {
        try {
            // Fetch user(s) from Keycloak based on the email
            List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().searchByEmail(email, true);

            // Map Keycloak UserRepresentation to your custom User DTO
            return userRepresentations.stream()
                    .map(UserMapper::toDto)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user by email: " + email, e);
        }
    }

}
