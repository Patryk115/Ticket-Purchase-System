package pl.edu.anstar.ticket_purchase.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.anstar.ticket_purchase.model.Ticket;
import pl.edu.anstar.ticket_purchase.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TicketGenerationWorker {

    @Autowired
    private TicketRepository ticketRepository;

    @JobWorker(type = "ticketGeneration")
    public Map<String, Object> generateTicket(final ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();

        Object finalPriceObj = vars.get("FinalPrice");
        if (finalPriceObj == null || !(finalPriceObj instanceof Number)) {
            throw new IllegalArgumentException("Brak zmiennej 'FinalPrice' lub jest nieprawidłowego typu.");
        }

        double finalPrice = ((Number) finalPriceObj).doubleValue();

        String cinemaLocation = (String) vars.getOrDefault("cinemaLocation", "Nieznane kino");
        int chosenRow = (int) vars.getOrDefault("chosenRow", 0);
        int chosenSeatNumber = (int) vars.getOrDefault("chosenSeatNumber", 0);

        String ticketId = "TCK-" + System.currentTimeMillis();

        Ticket ticket = new Ticket(ticketId, cinemaLocation, chosenRow, chosenSeatNumber, finalPrice, LocalDateTime.now());
        ticketRepository.save(ticket);

        System.out.println();
        System.out.println("===== Wygenerowano bilet =====");
        System.out.println(ticket);

        Map<String, Object> result = new HashMap<>();
        result.put("ticketId", ticket.getTicketId());
        result.put("ticketDetails", ticket.toString());

        System.out.println();

        return result;
    }
}
