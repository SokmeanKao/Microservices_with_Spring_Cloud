package org.example.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.model.User;
import org.example.model.request.UserRequest;
import org.example.model.request.UserUpdateRequest;
import org.example.model.response.ApiResponse;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:9090")
@SecurityRequirement(name = "keycloak_auth")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

//    @GetMapping("/get-user")
//    public String getGroup(){
//        return "Hello from user";
//    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        ApiResponse<List<User>> response = ApiResponse.<List<User>>builder()
                .status(HttpStatus.OK)
                .message("Get all users successfully!")
                .payload(userService.getAllUsers())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Create user
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody UserRequest userRequest) {
        ApiResponse<User> response = ApiResponse.<User>builder()
                .status(HttpStatus.CREATED)
                .message("Create user successfully!")
                .payload(userService.createUser(userRequest))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get user by ID
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable UUID userId) {
        ApiResponse<User> response = ApiResponse.<User>builder()
                .status(HttpStatus.OK)
                .message("Get user successfully!")
                .payload(userService.getUserById(userId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    // Update user by ID
    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable UUID userId, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        ApiResponse<User> response = ApiResponse.<User>builder()
                .status(HttpStatus.OK)
                .message("Update user successfully!")
                .payload(userService.updateUser(userId, userUpdateRequest))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Delete user by ID
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        String message = userService.deleteUser(userId);
        return ResponseEntity.ok(message);
    }

    // Get user(s) by username
    @GetMapping("users/username")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        List<User> users = userService.getUserByUsername(username);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found with username: " + username);
        }
        ApiResponse<List<User>> response = ApiResponse.<List<User>>builder()
                .status(HttpStatus.OK)
                .message("Get user by username successfully!")
                .payload(userService.getUserByUsername(username))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Get user(s) by email
    @GetMapping("users/email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        List<User> users = userService.getUserByEmail(email);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found with email: " + email);
        }
        ApiResponse<List<User>> response = ApiResponse.<List<User>>builder()
                .status(HttpStatus.OK)
                .message("Get user by email successfully!")
                .payload(userService.getUserByEmail(email))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
