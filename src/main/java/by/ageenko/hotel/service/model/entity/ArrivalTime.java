package by.ageenko.hotel.service.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "arrival_times")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_in")
    private String checkIn;

    @Column(name = "check_out")
    private String checkOut;
}