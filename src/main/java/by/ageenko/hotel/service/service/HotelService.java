package by.ageenko.hotel.service.service;

import by.ageenko.dto.CreateHotelRequest;
import by.ageenko.dto.HotelFullResponse;
import by.ageenko.dto.HotelShortResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public interface HotelService {
    List<HotelShortResponse> getAll();

    List<HotelShortResponse> getAllByParam(@Nullable String name,
                                           @Nullable String brand,
                                           @Nullable String city,
                                           @Nullable String country,
                                           @Nullable List<String> amenities);

    HotelFullResponse getHotelById(@NotNull Long id);

    HotelShortResponse createHotel(@NotNull CreateHotelRequest request);

    void addAmenitiesToHotel(@NotNull Long id,
                             @NotNull List<String> amenities);

    Map<String, Integer> getHistogram(@NotNull String param);
}
