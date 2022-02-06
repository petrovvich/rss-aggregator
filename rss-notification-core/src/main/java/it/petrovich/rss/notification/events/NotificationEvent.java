package it.petrovich.rss.notification.events;

public interface NotificationEvent<M> {

    M getBody();
}
