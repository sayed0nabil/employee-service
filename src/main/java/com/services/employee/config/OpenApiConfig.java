package com.services.employee.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3 / Swagger configuration.
 *
 * <p>Swagger UI is served at: {@code /swagger-ui.html}</p>
 * <p>Raw API docs at:         {@code /api-docs}</p>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Service API")
                        .description("""
                                RESTful CRUD API for managing employees.
                                
                                Features:
                                - Full CRUD operations
                                - In-memory storage (pre-seeded with 3 employees)
                                - RFC 7807 Problem Detail error responses
                                - Bean Validation on all request DTOs
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Employee Service Team")
                                .email("dev@company.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:3000")
                                .description("Local Development Server")
                ));
    }
}
