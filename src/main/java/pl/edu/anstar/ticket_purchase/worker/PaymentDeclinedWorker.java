package pl.edu.anstar.ticket_purchase.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentDeclinedWorker {

    @JobWorker(type = "paymentDeclined")
    public void handlePaymentDeclined(final ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();

        String paymentMessage = (String) vars.getOrDefault("paymentMessage", "Payment was declined.");

        System.out.println("===== Płatność odrzucona =====");
        System.out.println(paymentMessage);
        System.out.println("Spróbuj ponownie");
    }
}
