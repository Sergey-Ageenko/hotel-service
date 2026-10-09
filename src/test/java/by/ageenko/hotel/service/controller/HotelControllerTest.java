package by.ageenko.hotel.service.controller;

import by.ageenko.hotel.service.exception.InvalidDataException;
import by.ageenko.hotel.service.exception.NotFoundException;
import by.ageenko.hotel.service.model.constant.ApiErrorMessage;
import by.ageenko.hotel.service.model.dto.AddressDto;
import by.ageenko.hotel.service.model.dto.ArrivalTimeDto;
import by.ageenko.hotel.service.model.dto.ContactDto;
import by.ageenko.hotel.service.model.dto.request.CreateHotelRequest;
import by.ageenko.hotel.service.model.dto.response.HotelFullResponse;
import by.ageenko.hotel.service.model.dto.response.HotelShortResponse;
import by.ageenko.hotel.service.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HotelService hotelService;

    private HotelShortResponse shortResponse() {
        return new HotelShortResponse(
                1L,
                "Test Hotel",
                "Description",
                "10 Main Street, Minsk, 220000, Belarus",
                "+375 (29) 123-45-67"
        );
    }

    private HotelFullResponse fullResponse() {
        return new HotelFullResponse(
                1L,
                "Test Hotel",
                "Description",
                "Test brand",
                new AddressDto(10, "Main Street", "Minsk", "Belarus", "220000"),
                new ContactDto("+375 (29) 123-45-67", "test@example.com"),
                new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0)),
                List.of("Free WiFi", "Gym")
        );
    }

    private CreateHotelRequest createRequest() {
        return new CreateHotelRequest(
                "Test Hotel",
                "Description",
                "Test brand",
                new AddressDto(10, "Main Street", "Minsk", "Belarus", "220000"),
                new ContactDto("+375 (29) 123-45-67", "test@example.com"),
                new ArrivalTimeDto(LocalTime.of(14, 0), LocalTime.of(12, 0))
        );
    }


    @Test
    void getHotels_shouldReturnHotelsList() throws Exception {
        when(hotelService.getAll()).thenReturn(List.of(shortResponse()));
        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Hotel"));
        verify(hotelService).getAll();
    }

    @Test
    void getHotels_shouldReturnEmptyList_whenNoHotelsExist() throws Exception {
        when(hotelService.getAll()).thenReturn(List.of());
        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        verify(hotelService).getAll();
    }


    @Test
    void getHotelById_shouldReturnHotel_whenExists() throws Exception {
        when(hotelService.getHotelById(1L)).thenReturn(fullResponse());
        mockMvc.perform(get("/hotels/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Hotel"))
                .andExpect(jsonPath("$.brand").value("Test brand"))
                .andExpect(jsonPath("$.address.city").value("Minsk"))
                .andExpect(jsonPath("$.contacts.phone").value("+375 (29) 123-45-67"))
                .andExpect(jsonPath("$.amenities[0]").value("Free WiFi"));
        verify(hotelService).getHotelById(1L);
    }

    @Test
    void getHotelById_shouldReturnNotFound_whenHotelDoesNotExist() throws Exception {
        Long id = 999L;
        when(hotelService.getHotelById(id))
                .thenThrow(new NotFoundException(ApiErrorMessage.HOTEL_NOT_FOUND_BY_ID.getMessage(id)));
        mockMvc.perform(get("/hotels/{id}", id))
                .andExpect(status().isNotFound());
        verify(hotelService).getHotelById(id);
    }

    @Test
    void searchHotels_shouldReturnFilteredHotels() throws Exception {
        when(hotelService.getAllByParam(
                "Test brand",
                null,
                "Minsk",
                null,
                null
        )).thenReturn(List.of(shortResponse()));
        mockMvc.perform(get("/search")
                        .param("name", "Test brand")
                        .param("city", "Minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Hotel"));
        verify(hotelService).getAllByParam(
                "Test brand", null, "Minsk", null, null
        );
    }

    @Test
    void searchHotels_shouldPassAmenitiesParameter() throws Exception {
        when(hotelService.getAllByParam(
                null, null, null, null, List.of("Free WiFi", "Gym")
        )).thenReturn(List.of(shortResponse()));
        mockMvc.perform(get("/search")
                        .param("amenities", "Free WiFi", "Gym"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(hotelService).getAllByParam(
                null, null, null, null, List.of("Free WiFi", "Gym")
        );
    }

    @Test
    void createHotel_shouldReturnCreatedHotel() throws Exception {
        CreateHotelRequest request = createRequest();
        when(hotelService.createHotel(request)).thenReturn(shortResponse());
        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Hotel"));
        verify(hotelService).createHotel(request);
    }

    @Test
    void createHotel_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
        String requestJson = """
                {
                  "name": "",
                  "description": "Description",
                  "brand": "Test brand",
                  "address": {
                    "houseNumber": 10,
                    "street": "Main Street",
                    "city": "Minsk",
                    "country": "Belarus",
                    "postCode": "220000"
                  },
                  "contacts": {
                    "phone": "+375291234567",
                    "email": "test@example.com"
                  },
                  "amenities": []
                }
                """;
        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAmenitiesToHotel_shouldReturnSuccess() throws Exception {
        List<String> amenities = List.of("Free WiFi", "Gym");
        mockMvc.perform(post("/hotels/{id}/amenities", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amenities)))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        verify(hotelService).addAmenitiesToHotel(1L, amenities);
    }

    @Test
    void addAmenitiesToHotel_shouldReturnNotFound_whenHotelDoesNotExist() throws Exception {
        Long hotelId = 999L;
        List<String> amenities = List.of("Free WiFi");
        org.mockito.Mockito.doThrow(new NotFoundException(ApiErrorMessage.HOTEL_NOT_FOUND_BY_ID.getMessage(hotelId)))
                .when(hotelService).addAmenitiesToHotel(hotelId, amenities);
        mockMvc.perform(post("/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amenities)))
                .andExpect(status().isNotFound());
        verify(hotelService).addAmenitiesToHotel(hotelId, amenities);
    }

    @Test
    void getHistogram_shouldReturnBrandCounts() throws Exception {
        when(hotelService.getHistogram("brand"))
                .thenReturn(Map.of("Test brand", 3L, "Hilton", 2L));
        mockMvc.perform(get("/histogram/{param}", "brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Test brand']").value(3))
                .andExpect(jsonPath("$.Hilton").value(2));
        verify(hotelService).getHistogram("brand");
    }

    @Test
    void getHistogram_shouldReturnBadRequest_whenParameterUnsupported() throws Exception {
        when(hotelService.getHistogram("invalid"))
                .thenThrow(new InvalidDataException("Unsupported histogram parameter"));
        mockMvc.perform(get("/histogram/{param}", "invalid"))
                .andExpect(status().isBadRequest());
        verify(hotelService).getHistogram("invalid");
    }
}
