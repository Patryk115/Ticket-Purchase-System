package pl.edu.anstar.ticket_purchase.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class UserDataInputWorker {

    @JobWorker(type = "UserDataInput")
    public Map<String, Object> handleProvidingUserData(final ActivatedJob job) {
        System.out.println("===== Wprowadź swoje dane =====");

        Scanner scanner = new Scanner(System.in);

        String email = null;
        while (true) {
            System.out.print("Podaj adres e-mail na który zostanie wysłany bilet: ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Nieprawidłowy adres e-mail. Spróbuj ponownie.");
            }
        }


        System.out.println();

        System.out.println("Wybierz swój status:");
        System.out.println("[1] Uczeń");
        System.out.println("[2] Student");
        System.out.println("[3] Senior");
        System.out.println("[4] Pracownik");
        System.out.println("[5] Nie dotyczy");

        int category = 0;
        while (category < 1 || category > 5) {
            System.out.print("Wybierz opcję (1-5): ");
            category = Integer.parseInt(scanner.nextLine());
            if (category < 1 || category > 5) {
                System.out.println("Nieprawidłowa opcja. Spróbuj ponownie.");
            }
        }

        String categoryType;
        switch (category) {
            case 1:
                categoryType = "Uczeń";
                break;
            case 2:
                categoryType = "Student";
                break;
            case 3:
                categoryType = "Senior";
                break;
            case 4:
                categoryType = "Pracownik";
                break;
            case 5:
                categoryType = "Brak kategorii";
                break;
            default:
                throw new IllegalStateException("Nieoczekiwany błąd podczas wyboru kategorii.");
        }


        Map<String, Object> newVars = new HashMap<>();
        newVars.put("emailAddress", email);
        newVars.put("category", categoryType);

        System.out.println();

        return newVars;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }
}
