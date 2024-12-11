package org.example.configuration;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public CommandLineRunner openApiGroups(SwaggerUiConfigParameters swaggerUiParameters) {
        return args -> {
            swaggerUiParameters.addGroup("keycloak-admin-client");
            swaggerUiParameters.addGroup("task-service");
        };

    }
}
