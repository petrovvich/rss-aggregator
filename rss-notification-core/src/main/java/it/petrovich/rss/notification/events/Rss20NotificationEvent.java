package it.petrovich.rss.notification.events;

import it.petrovich.rss.xml.rss20111.TRssItem;

import java.util.UUID;

public record Rss20NotificationEvent(UUID eventId, TRssItem body) implements NotificationEvent<TRssItem> {

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public TRssItem getBody() {
        return body;
    }
}
