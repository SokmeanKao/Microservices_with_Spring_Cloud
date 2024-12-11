package org.example.client;

import org.example.model.response.ApiResponse;
import org.example.model.response.GroupResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "keycloak-admin-client")
@Primary
public interface GroupServiceClient {
    @GetMapping("/api/v1/group/{groupId}")
    ResponseEntity<ApiResponse<GroupResponse>> getGroupById(@PathVariable String groupId);
}
