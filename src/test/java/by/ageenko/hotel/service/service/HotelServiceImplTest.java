package by.ageenko.hotel.service.service;

import by.ageenko.hotel.service.exception.DataExistException;
import by.ageenko.hotel.service.exception.InvalidDataException;
import by.ageenko.hotel.service.exception.NotFoundException;
import by.ageenko.hotel.service.mapper.AmenityMapper;
import by.ageenko.hotel.service.mapper.HotelMapper;
import by.ageenko.hotel.service.model.constant.ApiErrorMessage;
import by.ageenko.hotel.service.model.dto.AddressDto;
import by.ageenko.hotel.service.model.dto.ArrivalTimeDto;
import by.ageenko.hotel.service.model.dto.ContactDto;
import by.ageenko.hotel.service.model.dto.HistogramProjection;
import by.ageenko.hotel.service.model.dto.request.CreateHotelRequest;
import by.ageenko.hotel.service.model.dto.response.HotelFullResponse;
import by.ageenko.hotel.service.model.dto.response.HotelShortResponse;
import by.ageenko.hotel.service.model.entity.Amenity;
import by.ageenko.hotel.service.model.entity.Hotel;
import by.ageenko.hotel.service.repository.AmenityRepository;
import by.ageenko.hotel.service.repository.HotelRepository;
import by.ageenko.hotel.service.repository.specification.HotelSpecification;
import by.ageenko.hotel.service.service.impl.HotelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceImplTest {
    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelMapper hotelMapper;

    @Mock
    private AmenityMapper amenityMapper;

    @Mock
    private HotelSpecification hotelSpecification;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void getHotelById_shouldReturnHotel_whenHotelExists() {
        Long id = 1L;

        Hotel hotel = Hotel.builder()
                .id(id)
                .name("Test Hotel")
                .build();

        HotelFullResponse expectedResponse = new HotelFullResponse(
                id,
                "Test Hotel",
                "Description",
                "Test Brand",
                null,
                null,
                null,
                List.of("Free WiFi")
        );

        when(hotelRepository.findByIdWithDetails(id))
                .thenReturn(Optional.of(hotel));

        when(hotelMapper.toFullResponse(hotel))
                .thenReturn(expectedResponse);

        HotelFullResponse result = hotelService.getHotelById(id);

        assertThat(result).isEqualTo(expectedResponse);

        verify(hotelRepository).findByIdWithDetails(id);
        verify(hotelMapper).toFullResponse(hotel);
    }

    @Test
    void getHotelById_shouldThrowException_whenHotelNotFound() {
        Long id = 999L;

        when(hotelRepository.findByIdWithDetails(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getHotelById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApiErrorMessage.HOTEL_NOT_FOUND_BY_ID.getMessage(id));

        verify(hotelRepository).findByIdWithDetails(id);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void createHotel_shouldReturnHotel_whenNameIsUnique() {
        CreateHotelRequest request = new CreateHotelRequest(
                "Test Hotel",
                "Description",
                "Test Brand",
                new AddressDto(10, "Main Street", "Minsk", "Belarus", "220000"),
                new ContactDto("+375 (29) 123-45-67", "test@example.com"),
                new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0))
        );

        Long id = 1L;

        Hotel hotel = Hotel.builder()
                .name(request.name())
                .build();

        Hotel savedHotel = Hotel.builder()
                .id(id)
                .name(request.name())
                .build();


        HotelShortResponse expectedResponse = new HotelShortResponse(
                id,
                "Test Hotel",
                "Description",
                "10 Main Street, Minsk, 220000, Belarus",
                "+375 (29) 123-45-67"
        );

        when(hotelRepository.existsByName(request.name())).thenReturn(false);
        when(hotelMapper.toHotel(request)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(savedHotel);
        when(hotelMapper.toShortResponse(savedHotel)).thenReturn(expectedResponse);

        HotelShortResponse result = hotelService.createHotel(request);

        assertThat(result).isEqualTo(expectedResponse);

        verify(hotelRepository).existsByName(request.name());
        verify(hotelMapper).toHotel(request);
        verify(hotelRepository).save(hotel);
        verify(hotelMapper).toShortResponse(savedHotel);
    }

    @Test
    void createHotel_shouldThrowException_whenHotelIsExist() {
        CreateHotelRequest request = new CreateHotelRequest(
                "Test Hotel",
                "Description",
                "Test Brand",
                new AddressDto(10, "Main Street", "Minsk", "Belarus", "220000"),
                new ContactDto("+375 (29) 123-45-67", "test@example.com"),
                new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0))
        );

        when(hotelRepository.existsByName(request.name()))
                .thenReturn(true);
        assertThatThrownBy(() -> hotelService.createHotel(request))
                .isInstanceOf(DataExistException.class)
                .hasMessage(ApiErrorMessage.HOTEL_WITH_NAME_ALREADY_EXIST.getMessage(request.name()));

        verify(hotelRepository).existsByName(request.name());
        verifyNoInteractions(hotelMapper);
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    void getAll_shouldReturnHotelsList() {
        Long id1 = 1L;
        Long id2 = 2L;

        Hotel hotel1 = Hotel.builder()
                .id(id1)
                .name("Test Hotel 1")
                .build();

        Hotel hotel2 = Hotel.builder()
                .id(id2)
                .name("Test Hotel 2")
                .build();

        List<HotelShortResponse> expectedResponse = List.of(
                new HotelShortResponse(id1,"Test Hotel 1", "Description", "10, Main Street, Minsk, Belarus, 220000", "+375 (29) 123-45-67"),
                new HotelShortResponse(id2,"Test Hotel 2", "Description", "10, Main Street, Minsk, Belarus, 220000", "+375 (29) 123-45-67")
        );

        when(hotelRepository.findAll())
                .thenReturn(List.of(hotel1,hotel2));

        when(hotelMapper.toShortResponse(hotel1))
                .thenReturn(expectedResponse.get(0));

        when(hotelMapper.toShortResponse(hotel2))
                .thenReturn(expectedResponse.get(1));

        List<HotelShortResponse> result = hotelService.getAll();

        assertThat(result).isEqualTo(expectedResponse);

        verify(hotelMapper).toShortResponse(hotel1);
        verify(hotelMapper).toShortResponse(hotel2);
        verify(hotelRepository).findAll();
    }

    @Test
    void getAllByParam_shouldReturnFilteredHotels() {
        String name = "Test Hotel";
        String brand = "Test brand";
        String city = "Minsk";
        String country = "Belarus";
        List<String> amenities = List.of("Free WiFi");

        Long id = 1L;
        Hotel hotel = Hotel.builder()
                .id(id)
                .name("Test Hotel")
                .build();

        HotelShortResponse expectedResponse = new HotelShortResponse(
                id,
                "Test Hotel",
                "Description",
                "10 Main Street, Minsk, 220000, Belarus",
                "+375 (29) 123-45-67"
        );

        Specification<Hotel> specification = Specification.unrestricted();

        when(hotelSpecification.hotelSearchSpecification(name, brand, city, country, amenities))
                .thenReturn(specification);

        when(hotelRepository.findAll(specification))
                .thenReturn(List.of(hotel));

        when(hotelMapper.toShortResponse(hotel))
                .thenReturn(expectedResponse);

        List<HotelShortResponse> result = hotelService.getAllByParam(name, brand, city, country, amenities);

        assertThat(result).containsExactly(expectedResponse);

        verify(hotelSpecification).hotelSearchSpecification(name, brand, city, country, amenities);
        verify(hotelRepository).findAll(specification);
        verify(hotelMapper).toShortResponse(hotel);
    }

    @Test
    void addAmenitiesToHotel_shouldAddOnlyNewAmenities() {
        Long hotelId = 1L;
        Long amenityId = 1L;

        Amenity existingAmenity = Amenity.builder()
                .id(amenityId)
                .name("Free WiFi")
                .build();

        Hotel hotel = Hotel.builder()
                .id(hotelId)
                .name("Test Hotel")
                .amenities(new ArrayList<>(List.of(existingAmenity)))
                .build();

        existingAmenity.setHotel(hotel);

        List<String> requestedAmenities = List.of(
                "Free WiFi",
                "Swimming Pool",
                "Swimming Pool",
                "Gym"
        );

        Amenity swimmingPool = Amenity.builder()
                .name("Swimming Pool")
                .hotel(hotel)
                .build();

        Amenity gym = Amenity.builder()
                .name("Gym")
                .hotel(hotel)
                .build();

        when(hotelRepository.findByIdWithAmenities(hotelId))
                .thenReturn(Optional.of(hotel));

        when(amenityMapper.toAmenity("Swimming Pool", hotel))
                .thenReturn(swimmingPool);

        when(amenityMapper.toAmenity("Gym", hotel))
                .thenReturn(gym);

        hotelService.addAmenitiesToHotel(hotelId, requestedAmenities);

        assertThat(hotel.getAmenities())
                .extracting(Amenity::getName)
                .containsExactly("Free WiFi", "Swimming Pool", "Gym");

        verify(hotelRepository).findByIdWithAmenities(hotelId);
        verify(amenityMapper).toAmenity("Swimming Pool", hotel);
        verify(amenityMapper).toAmenity("Gym", hotel);

        verifyNoInteractions(amenityRepository);
    }

    @Test
    void getHistogram_shouldReturnCounts() {
        HistogramProjection marriott = mock(HistogramProjection.class);
        HistogramProjection hilton = mock(HistogramProjection.class);

        when(marriott.getKey()).thenReturn("Marriott");
        when(marriott.getCount()).thenReturn(3L);

        when(hilton.getKey()).thenReturn("Hilton");
        when(hilton.getCount()).thenReturn(2L);

        when(hotelRepository.countGroupByBrand())
                .thenReturn(List.of(marriott, hilton));

        Map<String, Long> result = hotelService.getHistogram("brand");

        assertThat(result)
                .containsEntry("Marriott", 3L)
                .containsEntry("Hilton", 2L)
                .hasSize(2);

        verify(hotelRepository).countGroupByBrand();
        verifyNoInteractions(amenityRepository);
    }

    @Test
    void getHistogram_shouldThrowException_whenParameterUnsupported() {
        String param = "invalid";

        assertThatThrownBy(() -> hotelService.getHistogram(param))
                .isInstanceOf(InvalidDataException.class)
                .hasMessage(
                        ApiErrorMessage.UNSUPPORTED_HISTOGRAM_PARAMETER.getMessage(param)
                );

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(amenityRepository);
    }
}
