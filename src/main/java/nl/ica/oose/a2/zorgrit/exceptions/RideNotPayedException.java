package nl.ica.oose.a2.zorgrit.exceptions;

public class RideNotPayedException extends RuntimeException {
    public RideNotPayedException(String message) {
        super(message);
    }
}