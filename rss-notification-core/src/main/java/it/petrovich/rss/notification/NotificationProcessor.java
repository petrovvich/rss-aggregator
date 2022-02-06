package it.petrovich.rss.notification;

import it.petrovich.rss.notification.events.NotificationEvent;

public interface NotificationProcessor {
    String getType();

    boolean process(NotificationEvent<?> event);
}
