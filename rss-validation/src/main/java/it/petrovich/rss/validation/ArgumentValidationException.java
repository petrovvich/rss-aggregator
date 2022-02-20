package it.petrovich.rss.validation;

import lombok.Getter;

import java.text.MessageFormat;

/**
 * Error that uses when the target Object that stored in the field body has constraints violations.
 * Created in the aim to replace ConstraintViolationException with jakarta.validation package.
 * Spring Boot till version 3.0 doesn't recognize correctly jakarta.validation package.
 */
@Getter
public class ArgumentValidationException extends RuntimeException {
    /**
     * Template that stores message for the exception.
     * Fills by the object that contains constraints violations and fields that separated by the comma.
     */
    private static final String TEMPLATE = "Request object [{0}] has violations in fields [{1}]";

    /**
     * The target object that contains constraints violations.
     * Field is used for the logging and tracing purposes.
     */
    private final Object body;

    /**
     * Constructor for building the exception instance.
     * Calls super and fills the fields.
     *
     * @param body    object that contains constraints violations
     * @param message comma separated fields
     */
    public ArgumentValidationException(final Object body, final String message) {
        super(MessageFormat.format(TEMPLATE, body.getClass().getSimpleName(), message));
        this.body = body;
    }
}
