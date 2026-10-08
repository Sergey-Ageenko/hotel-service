package by.ageenko.hotel.service.service.impl;

import by.ageenko.hotel.service.exception.DataExistException;
import by.ageenko.hotel.service.exception.InvalidDataException;
import by.ageenko.hotel.service.exception.NotFoundException;
import by.ageenko.hotel.service.mapper.AmenityMapper;
import by.ageenko.hotel.service.mapper.HotelMapper;
import by.ageenko.hotel.service.model.constant.ApiErrorMessage;
import by.ageenko.hotel.service.model.dto.HistogramProjection;
import by.ageenko.hotel.service.model.dto.request.CreateHotelRequest;
import by.ageenko.hotel.service.model.dto.response.HotelFullResponse;
import by.ageenko.hotel.service.model.dto.response.HotelShortResponse;
import by.ageenko.hotel.service.model.entity.Amenity;
import by.ageenko.hotel.service.model.entity.Hotel;
import by.ageenko.hotel.service.repository.AmenityRepository;
import by.ageenko.hotel.service.repository.HotelRepository;
import by.ageenko.hotel.service.repository.specification.HotelSpecification;
import by.ageenko.hotel.service.service.HotelService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;
    private final AmenityMapper amenityMapper;
    private final HotelSpecification hotelSpecification;

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortResponse> getAll() {
        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::toShortResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortResponse> getAllByParam(@Nullable String name,
                                                  @Nullable String brand,
                                                  @Nullable String city,
                                                  @Nullable String country,
                                                  @Nullable List<String> amenities) {
        return hotelRepository.findAll(hotelSpecification.hotelSearchSpecification(name, brand, city, country, amenities))
                .stream()
                .map(hotelMapper::toShortResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public HotelFullResponse getHotelById(@NotNull Long id) {
        return hotelMapper.toFullResponse(hotelRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.HOTEL_NOT_FOUND_BY_ID.getMessage(id))));
    }

    @Override
    @Transactional
    public HotelShortResponse createHotel(CreateHotelRequest request) {
        if (hotelRepository.existsByName(request.name())) {
            throw new DataExistException(ApiErrorMessage.HOTEL_WITH_NAME_ALREADY_EXIST.getMessage(request.name()));
        }
        Hotel hotel = hotelMapper.toHotel(request);
        return hotelMapper
                .toShortResponse(hotelRepository.save(hotel));
    }

    @Override
    @Transactional
    public void addAmenitiesToHotel(Long id, List<String> amenities) {
        Hotel hotel = hotelRepository.findByIdWithAmenities(id)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.HOTEL_NOT_FOUND_BY_ID.getMessage(id)));
        Set<String> existingNames = hotel.getAmenities().stream()
                .map(Amenity::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        amenities.stream()
                .filter(name -> existingNames.add(name.toLowerCase()))
                .map(name -> amenityMapper.toAmenity(name, hotel))
                .forEach(hotel.getAmenities()::add);
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        List<HistogramProjection> result = switch (param.toLowerCase()) {
            case "brand" -> hotelRepository.countGroupByBrand();
            case "city" -> hotelRepository.countGroupByCity();
            case "country" -> hotelRepository.countGroupByCountry();
            case "amenities" -> amenityRepository.countGroupByAmenity();
            default -> throw new InvalidDataException(ApiErrorMessage.UNSUPPORTED_HISTOGRAM_PARAMETER.getMessage(param));
        };
        return result.stream()
                .collect(Collectors.toMap(
                        HistogramProjection::getKey,
                        HistogramProjection::getCount
                ));
    }
}
