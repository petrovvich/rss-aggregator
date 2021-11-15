package it.petrovich.rssprocessor.error;

import java.text.MessageFormat;

public class ArgumentValidationException extends RuntimeException {
    private static final String TEMPLATE = "Request object [{0}] has violations in fields [{1}]";
    private final Object body;

    public ArgumentValidationException(Object body, String message) {
        super(MessageFormat.format(TEMPLATE, body.getClass().getSimpleName(), message));
        this.body = body;
    }
}
