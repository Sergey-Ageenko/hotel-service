package by.ageenko.hotel.service.utils;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        @Schema(
                description = "HTTP status code",
                example = "400"
        )
        int status,

        @Schema(
                description = "HTTP error type",
                example = "BAD_REQUEST"
        )
        String error,

        @Schema(
                description = "Detailed error message",
                example = "Validation failed"
        )
        String message,

        @Schema(
                description = "Request URI that caused the error",
                example = "/property-view/hotels"
        )
        String path,

        @Schema(
                description = "Time when the error occurred",
                example = "2026-10-08T16:30:17.7447718"
        )
        LocalDateTime timestamp,

        @Schema(
                description = "Validation errors by field",
                example = "{\"name\":\"Name must not be blank\"}"
        )
        Map<String, String> validationErrors
) {
}
