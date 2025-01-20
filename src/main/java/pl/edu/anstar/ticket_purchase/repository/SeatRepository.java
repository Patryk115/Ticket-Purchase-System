package pl.edu.anstar.ticket_purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.anstar.ticket_purchase.model.Seat;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findByCinemaIdAndRowNumberAndSeatNumber(Long cinemaId, int rowNumber, int seatNumber);

    List<Seat> findAllByCinemaId(Long cinemaId);
}