package it.petrovich.rss.notification.events;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface NotificationEvent<M> {

    UUID getEventId();

    OffsetDateTime getEventDate();

    M getBody();
}
