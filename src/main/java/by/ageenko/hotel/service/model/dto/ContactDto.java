package by.ageenko.hotel.service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactDto(
        @Schema(
                description = "Hotel phone number",
                example = "+375 (17) 123-45-67"
        )
        @NotBlank(message = "Phone must not be blank")
        String phone,

        @Schema(
                description = "Hotel email",
                example = "info@hotel.com"
        )
        @Email(message = "Email must be valid")
        String email
) {
}
