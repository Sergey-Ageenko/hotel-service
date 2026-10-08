package by.ageenko.hotel.service.repository;

import by.ageenko.hotel.service.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
