package pl.edu.anstar.ticket_purchase.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cinema", schema = "cinema")
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CinemaShow> cinemaShows;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CinemaShow> getCinemaShows() {
        return cinemaShows;
    }

    public void setCinemaShows(List<CinemaShow> cinemaShows) {
        this.cinemaShows = cinemaShows;
    }
}