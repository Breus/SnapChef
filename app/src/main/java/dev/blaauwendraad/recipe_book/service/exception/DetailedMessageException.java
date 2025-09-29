package dev.blaauwendraad.recipe_book.service.exception;

public abstract class DetailedMessageException extends Exception {
    private final String detailMessage;

    protected DetailedMessageException(String message, String detailMessage) {
        super(message);
        this.detailMessage = detailMessage;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
