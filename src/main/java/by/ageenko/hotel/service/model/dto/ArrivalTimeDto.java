package by.ageenko.hotel.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record ArrivalTimeDto(
        @Schema(
                description = "Check-in time",
                example = "14:00"
        )
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkIn,

        @Schema(
                description = "Check-out time",
                example = "12:00"
        )
        @JsonFormat(pattern = "HH:mm")
        LocalTime checkOut
){
}
