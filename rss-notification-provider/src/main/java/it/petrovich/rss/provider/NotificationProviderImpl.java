package it.petrovich.rss.provider;

import it.petrovich.rss.notification.NotificationProvider;
import it.petrovich.rss.notification.NotificationService;
import it.petrovich.rss.notification.events.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProviderImpl implements NotificationProvider {
    private final Collection<NotificationService> clients;

    @Override
    public boolean send(final NotificationEvent<?> event) {
        try {
            clients.forEach(client -> client.sendEvent(event));
            return true;
        } catch (Exception e) {
            log.debug("Exception occurred while send event {}", event.getEventId(), e);
            return false;
        }
    }
}
