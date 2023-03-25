package edu.ntnu.idatt2105.backend.exception;

/**
 * Exception thrown when a conversation is not found.
 */
public class ConversationNotFoundException extends RuntimeException {
    public ConversationNotFoundException(String message) {
        super(message);
    }
}
