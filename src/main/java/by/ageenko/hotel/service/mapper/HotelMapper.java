package by.ageenko.hotel.service.mapper;

import by.ageenko.hotel.service.model.dto.request.CreateHotelRequest;
import by.ageenko.hotel.service.model.dto.response.HotelFullResponse;
import by.ageenko.hotel.service.model.dto.response.HotelShortResponse;
import by.ageenko.hotel.service.model.entity.Address;
import by.ageenko.hotel.service.model.entity.Amenity;
import by.ageenko.hotel.service.model.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortResponse toShortResponse(Hotel hotel);

    @Mapping(target = "address", source = "address")
    @Mapping(target = "contacts", source = "contacts")
    @Mapping(target = "arrivalTime", source = "arrivalTime")
    @Mapping(target = "amenities", source = "amenities")
    HotelFullResponse toFullResponse(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    Hotel toHotel(CreateHotelRequest request);

    default String addressToString(Address address) {
        if (address == null) return null;
        return String.format("%s %s, %s, %s, %s",
                address.getHouseNumber(), address.getStreet(),
                address.getCity(), address.getPostCode(), address.getCountry());
    }


    default List<String> amenitiesToStringList(List<Amenity> amenities) {
        return amenities.stream()
                .map(Amenity::getName)
                .toList();
    }

}

