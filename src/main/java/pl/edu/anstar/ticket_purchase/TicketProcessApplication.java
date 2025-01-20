package pl.edu.anstar.ticket_purchase;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Scanner;

@EnableScheduling
@SpringBootApplication
@EnableZeebeClient
@Deployment(resources = "classpath*:/model/*.*")
public class TicketProcessApplication implements CommandLineRunner {

    private final ZeebeClient zeebeClient;

    public TicketProcessApplication(@Qualifier("zeebeClientLifecycle") ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(TicketProcessApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println();

        System.out.print("Witaj w aplikacji do zakupu biletów kinowych. Naciśnij Enter, aby rozpocząć: ");
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            try {
                ProcessInstanceEvent processInstance = zeebeClient
                        .newCreateInstanceCommand()
                        .bpmnProcessId("Process_0q2oujw")
                        .latestVersion()
                        .send()
                        .join();

                System.out.println();
                System.out.println("Uruchomiono proces o kluczu: " + processInstance.getProcessInstanceKey());
            } catch (Exception e) {
                System.err.println("Błąd podczas uruchamiania procesu: " + e.getMessage());
            }
        } else {
            System.out.println("Proces nie został uruchomiony.");
        }
    }
}