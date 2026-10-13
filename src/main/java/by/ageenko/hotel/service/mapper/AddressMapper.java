package by.ageenko.hotel.service.mapper;

import by.ageenko.hotel.service.model.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Named("addressToString")
    default String addressToString(Address address) {
        return address == null ? null : String.format("%s %s, %s, %s, %s",
                address.getHouseNumber(), address.getStreet(),
                address.getCity(), address.getPostCode(), address.getCountry());
    }
}
