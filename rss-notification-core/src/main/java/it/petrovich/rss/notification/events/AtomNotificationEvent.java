package it.petrovich.rss.notification.events;

import it.petrovich.rss.xml.atom.EntryType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AtomNotificationEvent(UUID eventId, OffsetDateTime eventDate, EntryType body)
        implements NotificationEvent<EntryType> {

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public OffsetDateTime getEventDate() {
        return eventDate;
    }

    @Override
    public EntryType getBody() {
        return body;
    }
}
