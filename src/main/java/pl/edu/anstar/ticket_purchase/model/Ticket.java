package pl.edu.anstar.ticket_purchase.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets", schema="cinema")
public class Ticket {

    @Id
    private String ticketId;

    private String cinemaLocation;
    private int rowNumber;
    private int seatNumber;
    private double finalPrice;

    private LocalDateTime purchaseDate;

    public Ticket() {}

    public Ticket(String ticketId, String cinemaLocation, int rowNumber, int seatNumber, double finalPrice, LocalDateTime purchaseDate) {
        this.ticketId = ticketId;
        this.cinemaLocation = cinemaLocation;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.finalPrice = finalPrice;
        this.purchaseDate = purchaseDate;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getCinemaLocation() {
        return cinemaLocation;
    }

    public void setCinemaLocation(String cinemaLocation) {
        this.cinemaLocation = cinemaLocation;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %s, %s, Rząd: %d, Miejsce: %d, Cena: %.2f PLN, Data zakupu: %s",
                ticketId, cinemaLocation, rowNumber, seatNumber, finalPrice, purchaseDate
        );
    }
}
