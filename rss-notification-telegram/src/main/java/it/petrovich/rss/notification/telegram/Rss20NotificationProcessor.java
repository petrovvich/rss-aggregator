package it.petrovich.rss.notification.telegram;

import it.petrovich.rss.notification.NotificationProcessor;
import it.petrovich.rss.notification.events.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class Rss20NotificationProcessor implements NotificationProcessor {
    private final Collection<NotificationBot> bots;
    private final NotificationProperties notificationProperties;

    @Override
    public String getType() {
        return "TRssItem";
    }

    @Override
    public boolean process(final NotificationEvent<?> event) {
        bots.forEach(bot -> bot.sendMessage("TEST",
                notificationProperties.getChats().stream().findAny().get()));
        return true;
    }
}
