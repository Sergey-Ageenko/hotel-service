package by.ageenko.hotel.service.model.dto.request;

import by.ageenko.hotel.service.model.dto.AddressDto;
import by.ageenko.hotel.service.model.dto.ArrivalTimeDto;
import by.ageenko.hotel.service.model.dto.ContactDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateHotelRequest(
        @Schema(
                description = "Hotel name",
                example = "Grand Hotel Minsk"
        )
        @NotBlank(message = "Name must not be blank")
        String name,

        @Schema(
                description = "Hotel description",
                example = "Five-star hotel in the city center"
        )
        String description,

        @Schema(
                description = "Hotel brand",
                example = "Marriott"
        )
        @NotBlank(message = "Brand must not be blank")
        String brand,

        @Schema(description = "Hotel address")
        @NotNull(message = "Address must not be null")
        @Valid
        AddressDto address,

        @Schema(description = "Hotel contact information")
        @NotNull(message = "Contacts must not be null")
        @Valid
        ContactDto contacts,

        @Schema(
                description = "Hotel check-in and check-out times",
                implementation = ArrivalTimeDto.class
        )
        @Valid
        ArrivalTimeDto arrivalTime
) {
}
