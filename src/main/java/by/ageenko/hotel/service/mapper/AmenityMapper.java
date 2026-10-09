package by.ageenko.hotel.service.mapper;

import by.ageenko.hotel.service.model.entity.Amenity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AmenityMapper {

    @Mapping(target = "id", ignore = true)
    Amenity toAmenity(String name);
}
