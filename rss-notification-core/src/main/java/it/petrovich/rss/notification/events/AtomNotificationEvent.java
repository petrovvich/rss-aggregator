package it.petrovich.rss.notification.events;

import it.petrovich.rss.xml.atom.EntryType;

import java.util.UUID;

public record AtomNotificationEvent(UUID eventId, EntryType body) implements NotificationEvent<EntryType> {

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public EntryType getBody() {
        return body;
    }
}
