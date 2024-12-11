package org.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        servers = @Server(url = "/")
)
@SecurityScheme(
        name = "keycloak_auth", // Name of the security scheme
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                clientCredentials = @OAuthFlow(
                        tokenUrl = "http://localhost:8080/realms/master/protocol/openid-connect/token" // Token URL for Keycloak realm
                )
        )
)
public class KeyCloakAdminClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(KeyCloakAdminClientApplication.class, args);
    }
}