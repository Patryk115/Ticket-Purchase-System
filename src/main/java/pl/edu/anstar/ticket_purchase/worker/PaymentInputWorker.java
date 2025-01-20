package pl.edu.anstar.ticket_purchase.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class PaymentInputWorker {

    @JobWorker(type = "Payment Input")
    public Map<String, Object> handlePaymentInput(final ActivatedJob job) {
        System.out.println();
        System.out.println("===== Wprowadź Dane =====");

        Map<String, Object> oldVars = job.getVariablesAsMap();

        Scanner scanner = new Scanner(System.in);

        Object finalPriceObj = oldVars.get("FinalPrice");

        String firstName;
        do {
            System.out.print("Podaj imię: ");
            firstName = scanner.nextLine();
            if (!isValidName(firstName)) {
                System.out.println("Błędne imię. Spróbuj ponownie");
            }
        } while (!isValidName(firstName));

        String lastName;
        do {
            System.out.print("Podaj nazwisko: ");
            lastName = scanner.nextLine();
            if (!isValidName(lastName)) {
                System.out.println("Błędne nazwisko. Spróbuj ponownie ");
            }
        } while (!isValidName(lastName));

        String address;
        do {
            System.out.print("Podaj adres zamieszkania: ");
            address = scanner.nextLine();
            if (!isValidAddress(address)) {
                System.out.println("Błędny adres zamieszkania. Spróbuj ponownie ");
            }
        } while (!isValidAddress(address));

        String houseNumber;
        do {
            System.out.print("Podaj numer domu: ");
            houseNumber = scanner.nextLine();
            if (!isValidHouseNumber(houseNumber)) {
                System.out.println("Błędny numer domu. Spróbuj ponownie: ");
            }
        } while (!isValidHouseNumber(houseNumber));

        String postalCode;
        do {
            System.out.print("Podaj kod pocztowy (XX-XXX): ");
            postalCode = scanner.nextLine();
            if (!isValidPostalCode(postalCode)) {
                System.out.println("Błędny kod pocztowy. Spróbuj ponownie ");
            }
        } while (!isValidPostalCode(postalCode));

        System.out.println("Kwota do zapłaty: " + finalPriceObj + " zł");
        System.out.println();

        System.out.println("Wybierz metodę płatności:");
        System.out.println("[1] Karta płatnicza");
        System.out.println("[2] BLIK");
        System.out.println("[3] Przelew bankowy");
        int paymentMethod = 0;

        System.out.println();

        while (paymentMethod < 1 || paymentMethod > 3) {
            System.out.print("Wybierz opcję (1-3): ");
            paymentMethod = Integer.parseInt(scanner.nextLine());
            if (paymentMethod < 1 || paymentMethod > 3) {
                System.out.println("Nieprawidłowa opcja. Spróbuj ponownie.");
            }
        }

        Map<String, Object> newVars = new HashMap<>();
        newVars.put("firstName", firstName);
        newVars.put("lastName", lastName);
        newVars.put("address", address);
        newVars.put("houseNumber", houseNumber);
        newVars.put("postalCode", postalCode);
        newVars.put("paymentAmount", finalPriceObj);

        switch (paymentMethod) {
            case 1:
                System.out.print("Podaj numer karty płatniczej (np. 1234-5678-9012-3456): ");
                String cardNumber = scanner.nextLine();

                System.out.print("Podaj datę ważności karty (MM/YY): ");
                String cardExpiry = scanner.nextLine();

                System.out.print("Podaj kod CVV (3 cyfry): ");
                String cardCVV = scanner.nextLine();

                newVars.put("paymentMethod", "Card");
                newVars.put("cardNumber", cardNumber);
                newVars.put("cardExpiry", cardExpiry);
                newVars.put("cardCVV", cardCVV);
                break;

            case 2:
                System.out.print("Podaj 6-cyfrowy kod BLIK: ");
                String blikCode = scanner.nextLine();

                newVars.put("paymentMethod", "BLIK");
                newVars.put("blikCode", blikCode);
                break;

            case 3:
                System.out.print("Podaj numer konta bankowego (np. PL12345678901234567890123456): ");
                String bankAccount = scanner.nextLine();

                System.out.print("Podaj nazwę banku: ");
                String bankName = scanner.nextLine();

                newVars.put("paymentMethod", "Bank Transfer");
                newVars.put("bankAccount", bankAccount);
                newVars.put("bankName", bankName);
                break;

            default:
                throw new IllegalStateException("Nieoczekiwany błąd podczas wyboru metody płatności.");
        }

        return newVars;
    }

    private boolean isValidName(String name) {
        return name.matches("^[^0-9]+$");
    }

    private boolean isValidAddress(String address) {
        return address.matches("^[^0-9]+$");
    }

    private boolean isValidHouseNumber(String houseNumber) {
        return houseNumber.matches("^[0-9A-Za-z]+$");
    }

    private boolean isValidPostalCode(String postalCode) {
        return postalCode.matches("^[0-9]{2}-[0-9]{3}$");
    }
}
