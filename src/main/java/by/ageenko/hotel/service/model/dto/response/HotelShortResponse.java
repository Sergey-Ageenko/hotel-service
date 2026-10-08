package by.ageenko.hotel.service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Shorted hotel information")
public record HotelShortResponse(
        @Schema(description = "Unique hotel identifier", example = "1")
        Long id,

        @Schema(description = "Hotel name", example = "Marriott Minsk")
        String name,

        @Schema(
                description = "Hotel description",
                example = "Luxury hotel in the city center"
        )
        String description,

        @Schema(
                description = "Formatted hotel address",
                example = "20 Nezavisimosti Ave, Minsk, 220030, Belarus"
        )
        String address,

        @Schema(
                description = "Hotel phone number",
                example = "+375 (17) 309-30-90"
        )
        String phone
) {
}
