package pl.edu.anstar.ticket_purchase.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.anstar.ticket_purchase.model.Seat;
import pl.edu.anstar.ticket_purchase.model.SeatReservation;
import pl.edu.anstar.ticket_purchase.repository.SeatRepository;
import pl.edu.anstar.ticket_purchase.repository.SeatReservationRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SelectionOfSeatWorker {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatReservationRepository seatReservationRepository;

    @JobWorker(type = "SelectionOfSeat")
    public Map<String, Object> handleSelectionOfSeat(final ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        Long cinemaId = ((Number) variables.get("cinemaId")).longValue();
        Long cinemaShowId = ((Number) variables.get("cinemaShowId")).longValue();

        List<Seat> seats = seatRepository.findAllByCinemaId(cinemaId);

        List<SeatReservation> reservations = seatReservationRepository.findByCinemaShowId(cinemaShowId);
        Set<Long> occupiedSeatIds = reservations.stream()
                .filter(SeatReservation::isOccupied)
                .map(SeatReservation::getSeatId)
                .collect(Collectors.toSet());


        System.out.println("\n=====   Układ sali kinowej =====");
        System.out.print("    ");
        for (int seatNumber = 1; seatNumber <= 16; seatNumber++) {
            System.out.printf(" %2d  ", seatNumber);
        }
        System.out.println();


        for (int row = 1; row <= 6; row++) {
            System.out.printf("%2d  ", row);
            for (int seatNumber = 1; seatNumber <= 16; seatNumber++) {
                final int currentRow = row;
                final int currentSeatNumber = seatNumber;

                Optional<Seat> seatOpt = seats.stream()
                        .filter(s -> s.getRowNumber() == currentRow && s.getSeatNumber() == currentSeatNumber)
                        .findFirst();

                if (seatOpt.isPresent() && occupiedSeatIds.contains(seatOpt.get().getId())) {
                    System.out.print(" [X] ");
                } else {
                    System.out.print(" [ ] ");
                }
            }
            System.out.println();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nPodaj numer rzędu (1-6): ");
        int chosenRow = Integer.parseInt(scanner.nextLine());

        System.out.print("Podaj numer miejsca (1-16): ");
        int chosenSeatNumber = Integer.parseInt(scanner.nextLine());

        Map<String, Object> newVars = new HashMap<>();
        newVars.put("chosenRow", chosenRow);
        newVars.put("chosenSeatNumber", chosenSeatNumber);

        return newVars;
    }
}