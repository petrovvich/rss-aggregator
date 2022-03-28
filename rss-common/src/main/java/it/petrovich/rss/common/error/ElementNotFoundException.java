package it.petrovich.rss.common.error;

import java.text.MessageFormat;
import java.util.UUID;

public class ElementNotFoundException extends RuntimeException {

    public static final String TEMPLATE = "Can not find element in response for uuid [{0}]";

    public ElementNotFoundException(final UUID messageUuid) {
        super(MessageFormat.format(TEMPLATE, messageUuid));
    }
}
