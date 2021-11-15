package it.petrovich.rss.validation;

import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class ArgumentValidationException extends RuntimeException {
    private static final String TEMPLATE = "Request object [{0}] has violations in fields [{1}]";
    private final Object body;

    public ArgumentValidationException(Object body, String message) {
        super(MessageFormat.format(TEMPLATE, body.getClass().getSimpleName(), message));
        this.body = body;
    }
}
