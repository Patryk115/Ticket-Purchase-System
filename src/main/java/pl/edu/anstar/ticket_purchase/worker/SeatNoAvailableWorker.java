package pl.edu.anstar.ticket_purchase.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SeatNoAvailableWorker {

    @JobWorker(type = "SeatNoAvailable")
    public Map<String, Object> handleSeatNoAvailable(final ActivatedJob job) {

        System.out.println("\n===== Wybrane miejsce jest zajęte =====");

        return new HashMap<>();
    }
}