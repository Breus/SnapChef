package dev.blaauwendraad.recipe_book.service.exception;

public class UserAuthorizationException extends DetailedMessageException {

    public UserAuthorizationException(String detailMessage) {
        super("Failed to authorize user.", detailMessage);
    }
}
