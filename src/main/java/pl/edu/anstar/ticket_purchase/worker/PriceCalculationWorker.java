package pl.edu.anstar.ticket_purchase.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PriceCalculationWorker {

    @JobWorker(type = "priceCalculation")
    public Map<String, Object> calculateDiscount(final ActivatedJob job) {

        Map<String, Object> vars = job.getVariablesAsMap();



        Object discountValueObj = vars.get("CheckStatus");

        BigDecimal standardPrice = new BigDecimal("30.00");
        BigDecimal finalPrice;

        if (discountValueObj != null && discountValueObj instanceof Number) {
            double CheckStatus= ((Number) discountValueObj).doubleValue();
            System.out.println("Zniżka wynosi: " + CheckStatus + "%");

            BigDecimal discountFactor = BigDecimal.valueOf(1 - (CheckStatus / 100));
            finalPrice = standardPrice.multiply(discountFactor).setScale(2, BigDecimal.ROUND_HALF_UP);
            System.out.println("Cena Biletu: " + finalPrice + " zł");
        } else {
            System.out.println("Cena biletu: " + standardPrice + " zł");
            finalPrice = standardPrice;
        }

        Map<String, Object> newVars = new HashMap<>();
        newVars.put("FinalPrice", finalPrice);
        return newVars;
    }
}
