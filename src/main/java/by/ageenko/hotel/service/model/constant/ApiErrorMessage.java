package by.ageenko.hotel.service.model.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    HOTEL_NOT_FOUND_BY_ID("Hotel with id: %s was not found"),
    HOTEL_WITH_NAME_ALREADY_EXIST("Hotel with name: %s is already exist"),
    UNSUPPORTED_HISTOGRAM_PARAMETER("Unsupported histogram parameter: %s"),
    VALIDATION_FAILED("Validation failed");

    private final String message;

    public String getMessage(Object... args){
        return String.format(message, args);
    }
}