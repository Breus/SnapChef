package dev.blaauwendraad.recipe_book.service.exception;

public class UserRegistrationException extends Exception {
    // Additional field to store a detailed message
    private final String detailMessage;

    public UserRegistrationException(String detailMessage) {
        super("Failed to register new user.");
        this.detailMessage = detailMessage;
    }

    UserRegistrationException(String message, String detailMessage) {
        super(message);
        this.detailMessage = detailMessage;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
