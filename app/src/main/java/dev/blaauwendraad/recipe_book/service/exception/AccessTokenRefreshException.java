package dev.blaauwendraad.recipe_book.service.exception;

public class AccessTokenRefreshException extends DetailedMessageException {
    public AccessTokenRefreshException(String detailMessage) {
        super("Failed to refresh access token", detailMessage);
    }
}
