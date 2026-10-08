package by.ageenko.hotel.service.service;

import by.ageenko.hotel.service.exception.NotFoundException;
import by.ageenko.hotel.service.mapper.AmenityMapper;
import by.ageenko.hotel.service.mapper.HotelMapper;
import by.ageenko.hotel.service.model.constant.ApiErrorMessage;
import by.ageenko.hotel.service.model.dto.response.HotelFullResponse;
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

import java.util.List;
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
                .name("Marriott Minsk")
                .build();

        HotelFullResponse expectedResponse = new HotelFullResponse(
                id,
                "Marriott Minsk",
                "Luxury hotel",
                "Marriott",
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
}
