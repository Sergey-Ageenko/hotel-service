package by.ageenko.hotel.service.repository;

import by.ageenko.hotel.service.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
