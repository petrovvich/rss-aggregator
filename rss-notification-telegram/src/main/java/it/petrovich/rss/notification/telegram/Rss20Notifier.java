package it.petrovich.rss.notification.telegram;

import it.petrovich.rss.notification.NotificationProcessor;
import it.petrovich.rss.notification.events.NotificationEvent;
import it.petrovich.rss.notification.events.Rss20NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static it.petrovich.rss.domain.XmlUtils.extractOrElse;
import static it.petrovich.rss.domain.XmlUtils.extractParagraphs;

@Slf4j
@RequiredArgsConstructor
public class Rss20Notifier implements NotificationProcessor {
    private static final int COUNT_SENTENCES = 2;
    private static final String MSG_TEMPLATE = "<b>{0}</b>\n\n{1}\n\n<b><a href=\"{2}\">Read more</a></b>";

    private final NotificationProperties notificationProperties;
    private final Collection<NotificationBot> bots;

    @Override
    public String getType() {
        return "TRssItem";
    }

    @Override
    public boolean process(final NotificationEvent<?> event) {
        log.debug("Try to send event {}", event);
        bots.forEach(bot -> bot.sendMessage(prepareMessage(event),
                notificationProperties.getChats().stream().findAny().get()));
        return true;
    }

    private String prepareMessage(final NotificationEvent<?> event) {
        final var body = ((Rss20NotificationEvent) event).getBody();
        final var title = extractOrElse(body, "title", "").toString();
        final var description = formatDescription(extractParagraphs(extractOrElse(body, "description", "").toString()));
        final var link = extractOrElse(body, "link", "").toString();
        return TelegramMessage.builder()
                .link(link)
                .description(description)
                .title(title)
                .build()
                .format(MSG_TEMPLATE);
    }

    private String formatDescription(final String rawDescription) {
        return Arrays.stream(rawDescription.split("\\. "))
                .limit(COUNT_SENTENCES)
                .collect(Collectors.joining(".\n"))
                .concat(".");
    }
}
