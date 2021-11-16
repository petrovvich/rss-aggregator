package it.petrovich.rssprocessor.error;

public class UnknownRssTypeException extends RuntimeException {
    public UnknownRssTypeException(String message) {
        super(message);
    }
}
