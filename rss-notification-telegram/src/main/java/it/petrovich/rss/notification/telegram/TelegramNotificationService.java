package it.petrovich.rss.notification.telegram;

import it.petrovich.rss.notification.NotificationProcessor;
import it.petrovich.rss.notification.NotificationService;
import it.petrovich.rss.notification.events.NotificationEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static java.util.Optional.ofNullable;

@Slf4j
public record TelegramNotificationService(Map<String, NotificationProcessor> processors)
        implements NotificationService {

    @Override
    public boolean sendEvent(final NotificationEvent<?> event) {
        log.debug("Try to process notification event {}", event);
        return ofNullable(processors.get(event.getBody().getClass().getSimpleName()))
                .map(processor -> processor.process(event))
                .orElse(false);
    }
}
