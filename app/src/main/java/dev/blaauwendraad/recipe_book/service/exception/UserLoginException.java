package dev.blaauwendraad.recipe_book.service.exception;

public class UserLoginException extends DetailedMessageException {

    public UserLoginException(String detailMessage) {
        super("Failed to login user.", detailMessage);
    }
}
