package pl.edu.anstar.ticket_purchase.model;

import javax.persistence.*;

@Entity
@Table(name = "seat_reservations", schema = "cinema")
public class SeatReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @Column(name = "cinema_show_id", nullable = false)
    private Long cinemaShowId;

    @Column(name = "is_occupied", nullable = false)
    private boolean isOccupied;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Long getCinemaShowId() {
        return cinemaShowId;
    }

    public void setCinemaShowId(Long cinemaShowId) {
        this.cinemaShowId = cinemaShowId;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}