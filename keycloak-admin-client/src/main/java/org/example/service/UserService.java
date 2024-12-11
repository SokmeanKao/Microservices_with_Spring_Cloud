package org.example.service;

import jakarta.validation.Valid;
import org.example.model.User;
import org.example.model.request.UserRequest;
import org.example.model.request.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();

    User createUser(UserRequest userRequest);

    User getUserById(UUID id);

    User updateUser(UUID id, UserUpdateRequest userUpdateRequest);

    String deleteUser(UUID id);

    List<User> getUserByUsername(String username);

    List<User> getUserByEmail(String email);
}
