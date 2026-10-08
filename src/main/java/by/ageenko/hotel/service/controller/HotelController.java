package by.ageenko.hotel.service.controller;

import by.ageenko.hotel.service.model.dto.request.CreateHotelRequest;
import by.ageenko.hotel.service.model.dto.response.HotelFullResponse;
import by.ageenko.hotel.service.model.dto.response.HotelShortResponse;
import by.ageenko.hotel.service.service.HotelService;
import by.ageenko.hotel.service.utils.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(
        name = "Hotels",
        description = "Hotel management and search API"
)
@RestController
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @Operation(
            summary = "Get all hotels",
            description = "Returns a list of hotels with short information"
    )
    @ApiResponse(responseCode = "200", description = "Hotels retrieved successfully")
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelShortResponse>> getHotels() {
        return ResponseEntity.ok(hotelService.getAll());
    }

    @Operation(
            summary = "Search hotels",
            description = "Search hotels by name, brand, city, country and amenities"
    )
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @GetMapping("/search")
    public ResponseEntity<List<HotelShortResponse>> searchHotels(
            @Parameter(
                    description = "Hotel name. Case-insensitive partial search",
                    example = "Marriott"
            )
            @RequestParam(required = false) String name,

            @Parameter(
                    description = "Hotel brand. Case-insensitive exact search",
                    example = "Marriott"
            )
            @RequestParam(required = false) String brand,

            @Parameter(
                    description = "City. Case-insensitive exact search",
                    example = "Minsk"
            )
            @RequestParam(required = false) String city,

            @Parameter(
                    description = "Country. Case-insensitive exact search",
                    example = "Belarus"
            )
            @RequestParam(required = false) String country,

            @Parameter(
                    description = "Hotel amenities",
                    example = "Free WiFi"
            )
            @RequestParam(required = false) List<String> amenities) {
        return ResponseEntity.ok(hotelService.getAllByParam(name, brand, city, country, amenities));
    }

    @Operation(
            summary = "Get hotel by ID",
            description = "Returns detailed information about a hotel"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Hotel found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    name = "HotelNotFound",
                                    summary = "Hotel not found",
                                    value = """
                                            {
                                              "status": 404,
                                              "error": "NOT_FOUND",
                                              "message": "Hotel with id 999 was not found",
                                              "path": "/property-view/hotels/999",
                                              "timestamp": "2026-10-08T16:30:17",
                                              "validationErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelFullResponse> getHotelById(
            @Parameter(
                    description = "Unique hotel identifier",
                    example = "1"
            )
            @PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @Operation(
            summary = "Create hotel",
            description = "Creates a new hotel with optional description, address, contacts, and optional arrival time"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Hotel created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    @PostMapping("/hotels")
    public ResponseEntity<HotelShortResponse> createHotel(@RequestBody @Valid CreateHotelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.createHotel(request));
    }

    @Operation(
            summary = "Add amenities to hotel",
            description = "Adds one or more amenities to an existing hotel"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Amenities added successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hotel not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    name = "HotelNotFound",
                                    summary = "Hotel not found",
                                    value = """
                                            {
                                              "status": 404,
                                              "error": "NOT_FOUND",
                                              "message": "Hotel with id 999 was not found",
                                              "path": "/property-view/hotels/999",
                                              "timestamp": "2026-10-08T16:30:17",
                                              "validationErrors": null
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<Void> addAmenitiesToHotel(
            @Parameter(
                    description = "Unique hotel identifier",
                    example = "1"
            )
            @PathVariable Long id,
            @RequestBody List<String> amenities) {
        hotelService.addAmenitiesToHotel(id, amenities);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get hotel histogram",
            description = "Returns the number of hotels grouped by brand, city, country or amenities"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Histogram retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                 "Restaurant": 3,
                                                 "Fitness center": 2,
                                                 "Pool": 2,
                                                 "Free WiFi": 8
                                             }
                                            """
                            )

                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Unsupported histogram parameter",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)
                    )
            )
    })
    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String, Long>> getHistogram(
            @Parameter(
                    description = "Parameter to group hotels by",
                    required = true,
                    schema = @Schema(
                            type = "string",
                            allowableValues = {
                                    "brand",
                                    "city",
                                    "country",
                                    "amenities"
                            }
                    )
            )
            @PathVariable String param) {
        return ResponseEntity.ok(hotelService.getHistogram(param));
    }
}
