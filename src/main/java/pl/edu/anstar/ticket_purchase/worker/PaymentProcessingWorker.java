package pl.edu.anstar.ticket_purchase.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
public class PaymentProcessingWorker {

    @JobWorker(type = "paymentProcessing")
    public Map<String, Object> processPayment(final ActivatedJob job) {
        Map<String, Object> vars = job.getVariablesAsMap();

        Scanner scanner = new Scanner(System.in);
        boolean paymentApproved = false;

        while (!paymentApproved) {
            String paymentMethod = (String) vars.get("paymentMethod");

            if ("Card".equals(paymentMethod)) {
                paymentApproved = processCardPayment(vars, scanner);
            } else if ("BLIK".equals(paymentMethod)) {
                paymentApproved = processBlikPayment(vars, scanner);
            } else if ("Bank Transfer".equals(paymentMethod)) {
                paymentApproved = processBankTransfer(vars, scanner);
            } else {
                System.out.println("Nieznana metoda płatności. Spróbuj ponownie.");
            }

            if (!paymentApproved) {
                System.out.println("Płatność odrzucona. Wprowadź dane płatności ponownie.\n");
                vars = gatherPaymentData(scanner); // Pozwala użytkownikowi wprowadzić nowe dane płatności
            }
        }

        System.out.println("Płatność została zaakceptowana.");

        Map<String, Object> result = new HashMap<>();
        result.put("paymentApproved", paymentApproved);

        return result;
    }

    private boolean processCardPayment(Map<String, Object> vars, Scanner scanner) {
        String cardNumber = (String) vars.get("cardNumber");
        String cardExpiry = (String) vars.get("cardExpiry");
        String cardCVV = (String) vars.get("cardCVV");

        if (cardNumber != null && cardNumber.length() == 19 && cardExpiry != null && cardCVV != null) {
            System.out.println("Przetwarzanie płatności kartą...");
            return true;
        } else {
            System.out.println("Błąd w danych karty.");
            return false;
        }
    }

    private boolean processBlikPayment(Map<String, Object> vars, Scanner scanner) {
        String blikCode = (String) vars.get("blikCode");


        if (blikCode != null && blikCode.matches("\\d{6}")) {
            System.out.println("Przetwarzanie płatności BLIK...");
            return true;
        } else {
            System.out.println("Nieprawidłowy kod BLIK.");
            return false;
        }
    }

    private boolean processBankTransfer(Map<String, Object> vars, Scanner scanner) {
        String bankAccount = (String) vars.get("bankAccount");
        String bankName = (String) vars.get("bankName");


        if (bankAccount != null && bankAccount.startsWith("PL") && bankAccount.length() == 28) {
            System.out.println("Przetwarzanie płatności przelewem...");
            return true;
        } else {
            System.out.println("Nieprawidłowe dane przelewu bankowego.");
            return false;
        }
    }

    private Map<String, Object> gatherPaymentData(Scanner scanner) {
        Map<String, Object> newVars = new HashMap<>();

        System.out.println("===== Wprowadź dane płatności =====");

        System.out.println("[1] Karta płatnicza");
        System.out.println("[2] BLIK");
        System.out.println("[3] Przelew bankowy");

        int paymentMethod = 0;
        while (paymentMethod < 1 || paymentMethod > 3) {
            System.out.print("Wybierz metodę płatności (1-3): ");
            paymentMethod = Integer.parseInt(scanner.nextLine());
        }

        switch (paymentMethod) {
            case 1:
                System.out.print("Podaj numer karty płatniczej (np. 1234-5678-9012-3456): ");
                newVars.put("cardNumber", scanner.nextLine());

                System.out.print("Podaj datę ważności karty (MM/YY): ");
                newVars.put("cardExpiry", scanner.nextLine());

                System.out.print("Podaj kod CVV (3 cyfry): ");
                newVars.put("cardCVV", scanner.nextLine());

                newVars.put("paymentMethod", "Card");
                break;

            case 2:
                System.out.print("Podaj 6-cyfrowy kod BLIK: ");
                newVars.put("blikCode", scanner.nextLine());

                newVars.put("paymentMethod", "BLIK");
                break;

            case 3:
                System.out.print("Podaj numer konta bankowego (np. PL12345678901234567890123456): ");
                newVars.put("bankAccount", scanner.nextLine());

                System.out.print("Podaj nazwę banku: ");
                newVars.put("bankName", scanner.nextLine());

                newVars.put("paymentMethod", "Bank Transfer");
                break;
        }

        System.out.println();

        return newVars;
    }
}