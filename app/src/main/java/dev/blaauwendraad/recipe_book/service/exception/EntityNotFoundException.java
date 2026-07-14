package dev.blaauwendraad.recipe_book.service.exception;

public class EntityNotFoundException extends DetailedMessageException {

    public EntityNotFoundException(String detailMessage) {
        super("Entity not found.", detailMessage);
    }
}
