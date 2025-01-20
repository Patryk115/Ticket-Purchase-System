package pl.edu.anstar.ticket_purchase.exception;

/**
 * Klasa dla wyjątków walidacyjnych.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}