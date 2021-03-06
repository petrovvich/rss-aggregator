package it.petrovich.rss.domain.error;

import java.text.MessageFormat;

public class UnknownRssTypeException extends RuntimeException {
    private static final String TEMPLATE = "Unknown rss type [{0}]";

    public UnknownRssTypeException(final String type) {
        super(MessageFormat.format(TEMPLATE, type));
    }
}
