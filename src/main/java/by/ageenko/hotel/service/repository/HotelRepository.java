package by.ageenko.hotel.service.repository;

import by.ageenko.hotel.service.model.dto.HistogramProjection;
import by.ageenko.hotel.service.model.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    @Override
    @EntityGraph(attributePaths = {
            "address",
            "contacts"
    })
    List<Hotel> findAll();

    @Override
    @EntityGraph(attributePaths = {
            "address",
            "contacts"
    })
    List<Hotel> findAll(Specification<Hotel> specification);

    @EntityGraph(attributePaths = {
            "address",
            "contacts",
            "arrivalTime",
            "amenities"
    })
    @Query("""
            SELECT h FROM Hotel h
            WHERE h.id = :id
            """)
    Optional<Hotel> findByIdWithDetails(@Param("id") Long id);

    @EntityGraph(attributePaths = {"amenities"})
    @Query("""
            SELECT h FROM Hotel h
            WHERE h.id = :id
            """)
    Optional<Hotel> findByIdWithAmenities(@Param("id") Long id);

    boolean existsByName(String name);

    @Query("""
        SELECT h.brand AS key, COUNT(h) AS count
        FROM Hotel h
        GROUP BY h.brand
        """)
    List<HistogramProjection> countGroupByBrand();

    @Query("""
        SELECT h.address.city AS key, COUNT(h) AS count
        FROM Hotel h
        GROUP BY h.address.city
        """)
    List<HistogramProjection> countGroupByCity();

    @Query("""
        SELECT h.address.country AS key, COUNT(h) AS count
        FROM Hotel h
        GROUP BY h.address.country
        """)
    List<HistogramProjection> countGroupByCountry();
}