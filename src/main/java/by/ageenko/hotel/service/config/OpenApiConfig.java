package by.ageenko.hotel.service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Hotel Service",
                version = "1.0",
                description = "REST API for hotel management"
        )
)
@Configuration
public class OpenApiConfig {
}
