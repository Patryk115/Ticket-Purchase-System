package pl.edu.anstar.ticket_purchase.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TicketDeliveryWorker {

    @JobWorker(type = "ticketDelivery")
    public Map<String, Object> deliverTicket(final ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();
        String ticketId = (String) vars.get("ticketId");
        String emailAddress = (String) vars.getOrDefault("emailAddress", "unknown@nomail.com");



        System.out.println("Bilet został wysłany na adres " + emailAddress);
        System.out.println("Dziękujemy za zakup! Miłego seansu.");


        return new HashMap<>();
    }
}