package it.petrovich.rss.notification.telegram;

import it.petrovich.rss.notification.NotificationProcessor;
import it.petrovich.rss.notification.events.AtomNotificationEvent;
import it.petrovich.rss.notification.events.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static it.petrovich.rss.domain.XmlUtils.atomContentOrElse;
import static it.petrovich.rss.domain.XmlUtils.atomLinkOrElse;
import static it.petrovich.rss.domain.XmlUtils.extractParagraphs;
import static it.petrovich.rss.domain.XmlUtils.extractTextOrElse;

@Slf4j
@RequiredArgsConstructor
public class AtomNotifier implements NotificationProcessor {
    private static final String MSG_TEMPLATE = "<b>{0}</b>\n\n{1}\n\n<b><a href=\"{2}\">Read more</a></b>";
    private static final int COUNT_SENTENCES = 2;

    private final NotificationProperties notificationProperties;
    private final Collection<NotificationBot> bots;

    @Override
    public String getType() {
        return "FeedType";
    }

    @Override
    public boolean process(final NotificationEvent<?> event) {
        log.debug("Try to send event {}", event);
        bots.forEach(bot -> bot.sendMessage(prepareMessage(event),
                notificationProperties.getChats().stream().findAny().get()));
        return true;
    }

    private String prepareMessage(final NotificationEvent<?> event) {
        final var body = ((AtomNotificationEvent) event).getBody();
        final var title = extractTextOrElse(body, "title", "");
        final var link = atomLinkOrElse(body, "link", "");
        final var description = formatDescription(extractParagraphs(atomContentOrElse(body, "content", "")));

        return TelegramMessage.builder()
                .description(description)
                .title(title)
                .link(link)
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
