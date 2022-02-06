package it.petrovich.rssprocessor.events;

import it.petrovich.rss.notification.events.NotificationEvent;
import it.petrovich.rss.xml.rss20111.TRssItem;

public record Rss20NotificationEvent(TRssItem body) implements NotificationEvent<TRssItem> {

    @Override
    public TRssItem getBody() {
        return body;
    }
}