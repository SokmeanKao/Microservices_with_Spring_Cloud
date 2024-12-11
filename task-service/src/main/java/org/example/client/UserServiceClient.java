package org.example.client;

import org.example.model.response.ApiResponse;
import org.example.model.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "keycloak-admin-client")
@Primary
public interface UserServiceClient {
    @GetMapping("api/v1/user/users/{userId}")
    ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String userId);
}
