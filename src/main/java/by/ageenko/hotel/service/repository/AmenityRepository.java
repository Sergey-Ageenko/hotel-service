package by.ageenko.hotel.service.repository;

import by.ageenko.hotel.service.model.dto.HistogramProjection;
import by.ageenko.hotel.service.model.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    @Query("""
            SELECT a.name AS key, COUNT(a) AS count
            FROM Amenity a
            GROUP BY a.name
            """)
    List<HistogramProjection> countGroupByAmenity();
}
