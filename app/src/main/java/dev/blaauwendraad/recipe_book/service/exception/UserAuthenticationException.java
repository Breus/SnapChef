package dev.blaauwendraad.recipe_book.service.exception;

public class UserAuthenticationException extends DetailedMessageException {

    public UserAuthenticationException(String detailMessage) {
        super("Failed to authenticate user.", detailMessage);
    }
}
