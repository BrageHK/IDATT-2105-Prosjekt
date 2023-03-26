package edu.ntnu.idatt2105.backend.exception;

/**
 * Exception thrown when a User is not found.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
