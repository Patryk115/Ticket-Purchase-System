package pl.edu.anstar.ticket_purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.anstar.ticket_purchase.model.SeatReservation;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {

    Optional<SeatReservation> findBySeatIdAndCinemaShowId(Long seatId, Long cinemaShowId);

    List<SeatReservation> findByCinemaShowId(Long cinemaShowId);
}