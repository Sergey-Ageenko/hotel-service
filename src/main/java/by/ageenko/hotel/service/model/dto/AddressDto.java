package by.ageenko.hotel.service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressDto(
        @Schema(
                description = "Building number",
                example = "20"
        )
        @NotNull(message = "House number must not be null")
        Integer houseNumber,

        @Schema(
                description = "Street name",
                example = "Nezavisimosti Avenue"
        )
        @NotBlank(message = "Street must not be blank")
        String street,

        @Schema(
                description = "City",
                example = "Minsk"
        )
        @NotBlank(message = "City must not be blank")
        String city,

        @Schema(
                description = "Country",
                example = "Belarus"
        )
        @NotBlank(message = "Country must not be blank")
        String country,

        @Schema(
                description = "Postal code",
                example = "220030"
        )
        @NotBlank(message = "Post code must not be blank")
        String postCode
){
}
