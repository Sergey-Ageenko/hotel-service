package by.ageenko.hotel.service.mapper;

import by.ageenko.dto.CreateHotelRequest;
import by.ageenko.dto.HotelFullResponse;
import by.ageenko.dto.HotelShortResponse;
import by.ageenko.hotel.service.model.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {AddressMapper.class, AmenityMapper.class})
public interface HotelMapper {

    @Mapping(target = "address", source = "address", qualifiedByName = "addressToString")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortResponse toShortResponse(Hotel hotel);

    HotelFullResponse toFullResponse(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    Hotel toHotel(CreateHotelRequest request);

}

