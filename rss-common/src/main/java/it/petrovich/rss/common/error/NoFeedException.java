package it.petrovich.rss.common.error;

import java.text.MessageFormat;

public class NoFeedException extends RuntimeException {
    private static final String TEMPLATE = "Can not found RSS subscription for request [{0}]";

    public NoFeedException(final Object body) {
        super(MessageFormat.format(TEMPLATE, body));
    }
}
