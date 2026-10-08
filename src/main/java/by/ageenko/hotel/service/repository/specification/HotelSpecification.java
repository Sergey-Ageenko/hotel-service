package by.ageenko.hotel.service.repository.specification;

import by.ageenko.hotel.service.model.entity.Amenity;
import by.ageenko.hotel.service.model.entity.Hotel;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HotelSpecification {

    public Specification<Hotel> hotelSearchSpecification(
            @Nullable String name,
            @Nullable String brand,
            @Nullable String city,
            @Nullable String country,
            @Nullable List<String> amenities
    ) {
        return Specification.allOf(
                hasName(name),
                hasBrand(brand),
                hasCity(city),
                hasCountry(country),
                hasAmenities(amenities)
        );
    }

    private static Specification<Hotel> hasName(String name) {
        return (root, query, cb) ->
                name == null || name.isBlank()
                        ? cb.conjunction()
                        : cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    private static Specification<Hotel> hasBrand(String brand) {
        return (root, query, cb) ->
                brand == null || brand.isBlank()
                        ? cb.conjunction()
                        : cb.equal(
                        cb.lower(root.get("brand")),
                        brand.toLowerCase()
                );
    }

    private static Specification<Hotel> hasCity(String city) {
        return (root, query, cb) ->
                city == null || city.isBlank()
                        ? cb.conjunction()
                        : cb.equal(
                        cb.lower(root.get("address").get("city")),
                        city.toLowerCase()
                );
    }

    private static Specification<Hotel> hasCountry(String country) {
        return (root, query, cb) ->
                country == null || country.isBlank()
                        ? cb.conjunction()
                        : cb.equal(
                        cb.lower(root.get("address").get("country")),
                        country.toLowerCase()
                );
    }

    private static Specification<Hotel> hasAmenities(List<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return cb.conjunction();
            }
            query.distinct(true);
            Join<Hotel, Amenity> amenityJoin = root.join("amenities");
            return cb.lower(amenityJoin.get("name"))
                    .in(amenities.stream()
                            .map(String::toLowerCase)
                            .toList()
            );
        };
    }
}
