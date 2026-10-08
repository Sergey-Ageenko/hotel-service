package by.ageenko.hotel.service.mapper;

import by.ageenko.hotel.service.model.entity.Amenity;
import by.ageenko.hotel.service.model.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AmenityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "hotel", source = "hotel")
    Amenity toAmenity(String name, Hotel hotel);
}
