package by.ageenko.hotel.service.controller;


import by.ageenko.api.HotelApi;
import by.ageenko.dto.CreateHotelRequest;
import by.ageenko.dto.HotelFullResponse;
import by.ageenko.dto.HotelShortResponse;
import by.ageenko.hotel.service.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HotelController implements HotelApi {

    private final HotelService hotelService;

    @Override
    public List<HotelShortResponse> getHotels() {
        return hotelService.getAll();
    }

    @Override
    public List<HotelShortResponse> searchHotels(
            @Nullable String name,
            @Nullable String brand,
            @Nullable String city,
            @Nullable String country,
            @Nullable List<String> amenities) {
        return hotelService.getAllByParam(name, brand, city, country, amenities);
    }

    @Override
    public HotelFullResponse getHotelById(Long id) {
        return hotelService.getHotelById(id);
    }

    @Override
    public HotelShortResponse createHotel(CreateHotelRequest request) {
        return hotelService.createHotel(request);
    }


    @Override
    public void addAmenitiesToHotel(Long id, List<String> amenities) {
        hotelService.addAmenitiesToHotel(id, amenities);
    }

    @Override
    public Map<String, Integer> getHistogram(String param) {
        return hotelService.getHistogram(param);
    }
}
