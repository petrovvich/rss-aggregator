package it.petrovich.rss.notification.events;

import java.util.UUID;

public interface NotificationEvent<M> {

    UUID getEventId();

    M getBody();
}
