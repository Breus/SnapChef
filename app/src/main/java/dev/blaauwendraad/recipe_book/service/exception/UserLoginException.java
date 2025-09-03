package dev.blaauwendraad.recipe_book.service.exception;

public class UserLoginException extends Exception {
    private final String detailMessage;

    public UserLoginException(String detailMessage) {
        super("Failed to login user.");
        this.detailMessage = detailMessage;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
