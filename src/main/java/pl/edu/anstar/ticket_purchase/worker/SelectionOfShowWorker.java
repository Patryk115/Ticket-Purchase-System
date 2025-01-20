package pl.edu.anstar.ticket_purchase.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;
import pl.edu.anstar.ticket_purchase.model.Cinema;
import pl.edu.anstar.ticket_purchase.model.CinemaShow;
import pl.edu.anstar.ticket_purchase.repository.CinemaRepository;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class SelectionOfShowWorker {

    private final CinemaRepository cinemaRepository;

    public SelectionOfShowWorker(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @Transactional
    @JobWorker(type = "SelectionOfShow")
    public Map<String, Object> handleSelectionOfShow(final ActivatedJob job) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Cinema> cinemas = cinemaRepository.findAll();
        System.out.println("\n===== REPERTUARY KIN W OKOLICY  =====");
        for (int i = 0; i < cinemas.size(); i++) {
            Cinema cinema = cinemas.get(i);
            System.out.printf("[%d] %s%n", i + 1, cinema.getName());
            List<CinemaShow> shows = cinema.getCinemaShows();
            for (CinemaShow show : shows) {
                String formattedShowTime = show.getShowTime().format(formatter);
                System.out.printf("- %s (%s)%n", show.getMovieTitle(), formattedShowTime);
            }
            System.out.println();
        }

        Scanner scanner = new Scanner(System.in);
        Cinema chosenCinema;
        int cinemaChoice;
        while (true) {
            try {
                System.out.print("Wybierz numer kina: ");
                cinemaChoice = Integer.parseInt(scanner.nextLine());
                chosenCinema = cinemas.get(cinemaChoice - 1);
                break;
            } catch (Exception e) {
                System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }

        System.out.printf("Wybrano: %s%n%n", chosenCinema.getName());
        System.out.println("Dostępne filmy");
        List<CinemaShow> chosenCinemaShows = chosenCinema.getCinemaShows();
        for (int i = 0; i < chosenCinemaShows.size(); i++) {
            CinemaShow show = chosenCinemaShows.get(i);
            String formattedShowTime = show.getShowTime().format(formatter);
            System.out.printf("[%d] %s (%s)%n", i + 1, show.getMovieTitle(), formattedShowTime);
        }

        System.out.println();

        CinemaShow chosenShow;
        while (true) {
            try {
                System.out.print("Wybierz numer seansu: ");
                int showChoice = Integer.parseInt(scanner.nextLine());
                chosenShow = chosenCinemaShows.get(showChoice - 1);
                break;
            } catch (Exception e) {
                System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
            }
        }

        System.out.println();

        String formattedChosenShowTime = chosenShow.getShowTime().format(formatter);
        System.out.printf("Wybrano seans: %s (%s)%n", chosenShow.getMovieTitle(), formattedChosenShowTime);


        return Map.of(
                "cinemaLocation", chosenCinema.getName(),
                "movieTitle", chosenShow.getMovieTitle(),
                "showTime", formattedChosenShowTime,
                "cinemaId", chosenCinema.getId(),
                "cinemaShowId", chosenShow.getId()
        );
    }
}