package by.ageenko.hotel.service.model.dto.response;

import by.ageenko.hotel.service.model.dto.AddressDto;
import by.ageenko.hotel.service.model.dto.ArrivalTimeDto;
import by.ageenko.hotel.service.model.dto.ContactDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Detailed hotel information")
public record HotelFullResponse(
        @Schema(
                description = "Unique hotel identifier",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Hotel name",
                example = "Marriott Minsk"
        )
        String name,

        @Schema(
                description = "Hotel description",
                example = "Luxury hotel in the city center"
        )
        String description,

        @Schema(
                description = "Hotel brand",
                example = "Marriott"
        )
        String brand,

        @Schema(description = "Hotel address")
        AddressDto address,

        @Schema(description = "Hotel contact information")
        ContactDto contacts,

        @Schema(description = "Hotel check-in and check-out times")
        ArrivalTimeDto arrivalTime,

        @Schema(
                description = "List of hotel amenities",
                example = "[\"Free WiFi\", \"Pool\", \"Restaurant\"]"
        )
        List<String> amenities
) {
}
