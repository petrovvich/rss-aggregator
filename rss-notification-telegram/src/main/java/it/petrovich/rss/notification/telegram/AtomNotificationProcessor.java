package it.petrovich.rss.notification.telegram;

import it.petrovich.rss.notification.NotificationProcessor;
import it.petrovich.rss.notification.events.AtomNotificationEvent;
import it.petrovich.rss.notification.events.NotificationEvent;
import it.petrovich.rss.xml.XmlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static it.petrovich.rss.xml.XmlUtils.extractParagraphs;
import static java.text.MessageFormat.format;

@Slf4j
@RequiredArgsConstructor
public class AtomNotificationProcessor implements NotificationProcessor {
    private static final String MSG_TEMPLATE = "<b>{0}</b>\n\n{1}\n\n<b><a href=\"{2}\">Read more</a></b>";
    private static final int COUNT_SENTENCES = 2;

    private final NotificationProperties notificationProperties;
    private final Collection<NotificationBot> bots;

    @Override
    public String getType() {
        return "FeedType";
    }

    @Override
    public boolean process(NotificationEvent<?> event) {
        log.debug("Try to send event {}", event);
        bots.forEach(bot -> bot.sendMessage(prepareMessage(event),
                notificationProperties.getChats().stream().findAny().get()));
        return true;
    }

    private String prepareMessage(final NotificationEvent<?> event) {
        val body = ((AtomNotificationEvent) event).getBody();
        val title = XmlUtils.extractTextOrElse(body, "title", "").toString();
        val description = formatDescription(extractParagraphs(XmlUtils.extractTextOrElse(body, "description", "").toString()));
        val link = XmlUtils.atomLinkOrElse(body, "link", "").toString();
        return format(MSG_TEMPLATE, title, description, link);
    }

    private String formatDescription(final String rawDescription) {
        return Arrays.stream(rawDescription.split("\\. "))
                .limit(COUNT_SENTENCES)
                .collect(Collectors.joining(".\n"))
                .concat(".");
    }
}
