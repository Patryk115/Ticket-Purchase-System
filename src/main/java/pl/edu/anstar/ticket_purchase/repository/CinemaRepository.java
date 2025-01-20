package pl.edu.anstar.ticket_purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.anstar.ticket_purchase.model.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
}