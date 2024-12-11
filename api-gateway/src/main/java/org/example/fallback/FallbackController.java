package org.example.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @GetMapping("/fallback/keyCloakAdminClient")
    public ResponseEntity<?> fallback() {
        return new ResponseEntity<>("Keycloak admin client Service is down. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
    @GetMapping("/fallback/task")
    public ResponseEntity<?> fallbackCustomer() {
        return new ResponseEntity<>("Task Service is down. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
