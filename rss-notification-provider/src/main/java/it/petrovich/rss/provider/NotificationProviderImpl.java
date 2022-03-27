package it.petrovich.rss.provider;

import it.petrovich.rss.notification.NotificationProvider;
import it.petrovich.rss.notification.NotificationService;
import it.petrovich.rss.notification.events.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProviderImpl implements NotificationProvider {
    private final Queue<NotificationEvent<?>> queue = new ConcurrentLinkedQueue<>();
    private final Collection<NotificationService> clients;

    @Override
    public boolean send(final NotificationEvent<?> event) {
        try {
            queue.add(event);
            return true;
        } catch (Exception e) {
            log.debug("Exception occurred while send event {}", event.getEventId(), e);
            return false;
        }
    }

    @Scheduled(cron = "${rss.events.send.cron}")
    public void processEntries() {
        try {
            ofNullable(queue.poll())
                    .ifPresent(event -> clients.forEach(client -> client.sendEvent(event)));
        } catch (Exception e) {
            log.debug("Exception occurred while send event {}", null, e);
        }
    }
}
