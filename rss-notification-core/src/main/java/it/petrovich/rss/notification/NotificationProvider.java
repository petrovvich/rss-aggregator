package it.petrovich.rss.notification;

import it.petrovich.rss.notification.events.NotificationEvent;

public interface NotificationProvider {

    boolean send(NotificationEvent<?> event);
}
