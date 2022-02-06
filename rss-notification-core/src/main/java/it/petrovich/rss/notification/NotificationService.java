package it.petrovich.rss.notification;

import it.petrovich.rss.notification.events.NotificationEvent;

public interface NotificationService {

    boolean sendEvent(NotificationEvent<?> event);
}
