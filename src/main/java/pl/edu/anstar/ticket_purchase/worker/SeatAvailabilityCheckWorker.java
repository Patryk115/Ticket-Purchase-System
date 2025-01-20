package pl.edu.anstar.ticket_purchase.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.anstar.ticket_purchase.model.Seat;
import pl.edu.anstar.ticket_purchase.model.SeatReservation;
import pl.edu.anstar.ticket_purchase.repository.SeatRepository;
import pl.edu.anstar.ticket_purchase.repository.SeatReservationRepository;

import java.util.Map;
import java.util.Optional;

@Service
public class SeatAvailabilityCheckWorker {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatReservationRepository seatReservationRepository;

    @JobWorker(type = "seatAvailabilityCheck")
    public Map<String, Object> handleSeatCheck(final ActivatedJob job) {

        Map<String, Object> vars = job.getVariablesAsMap();

        Number cinemaIdNum       = (Number) vars.get("cinemaId");
        Number cinemaShowIdNum   = (Number) vars.get("cinemaShowId");
        Number rowNumberNum      = (Number) vars.get("chosenRow");
        Number seatNumberNum     = (Number) vars.get("chosenSeatNumber");

        if (cinemaIdNum == null || cinemaShowIdNum == null
                || rowNumberNum == null || seatNumberNum == null) {
            throw new IllegalStateException("Brak wymaganych zmiennych w kontekście procesu (cinemaId/cinemaShowId/chosenRow/chosenSeatNumber).");
        }

        long cinemaId     = cinemaIdNum.longValue();
        long cinemaShowId = cinemaShowIdNum.longValue();
        int rowNumber     = rowNumberNum.intValue();
        int seatNumber    = seatNumberNum.intValue();


        boolean seatAvailable = false;

        Optional<Seat> seatOpt = seatRepository.findByCinemaIdAndRowNumberAndSeatNumber(
                cinemaId, rowNumber, seatNumber
        );

        if (seatOpt.isEmpty()) {
            System.out.println("Takie miejsce nie istnieje w tym kinie!");
            seatAvailable = false;
        } else {
            Seat seat = seatOpt.get();
            Optional<SeatReservation> existingRes = seatReservationRepository
                    .findBySeatIdAndCinemaShowId(seat.getId(), cinemaShowId);

            if (existingRes.isPresent()) {
                if (existingRes.get().isOccupied()) {
                    seatAvailable = false;
                } else {
                    SeatReservation sr = existingRes.get();
                    sr.setOccupied(true);
                    seatReservationRepository.save(sr);
                    System.out.printf("Zarezerwowano miejsce: Rząd %d, Miejsce %d", rowNumber, seatNumber);
                    seatAvailable = true;
                }
            } else {
                SeatReservation sr = new SeatReservation();
                sr.setSeatId(seat.getId());
                sr.setCinemaShowId(cinemaShowId);
                sr.setOccupied(true);
                seatReservationRepository.save(sr);

                System.out.printf("Zarezerwowano miejsce: Rząd %d, Miejsce %d%n", rowNumber, seatNumber);
                seatAvailable = true;
            }
        }

        System.out.println();

        return Map.of("seatAvailable", seatAvailable);
    }
}