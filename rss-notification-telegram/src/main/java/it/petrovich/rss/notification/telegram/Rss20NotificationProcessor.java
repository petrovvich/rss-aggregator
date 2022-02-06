package it.petrovich.rss.notification.telegram;

import it.petrovich.rss.notification.NotificationProcessor;
import it.petrovich.rss.notification.events.NotificationEvent;

public class Rss20NotificationProcessor implements NotificationProcessor {
    @Override
    public String getType() {
        return "TRssItem";
    }

    @Override
    public boolean process(NotificationEvent<?> event) {
        return false;
    }
}
