package it.petrovich.rssprocessor.processor;

import it.petrovich.rss.common.FeedEntry;
import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.ProcessingResult;
import it.petrovich.rss.common.RssType;
import it.petrovich.rss.notification.NotificationService;
import it.petrovich.rss.xml.rss20111.TRss;
import it.petrovich.rssprocessor.events.Rss20NotificationEvent;
import it.petrovich.rssprocessor.storage.RssStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static it.petrovich.rss.common.RssType.RSS20;

@Slf4j
@Component
@RequiredArgsConstructor
public final class Rss20Processor implements FeedProcessor {
    private final RssStorage storage;
    private final NotificationService notificationService;

    @Override
    public RssType getType() {
        return RSS20;
    }

    @Override
    public ProcessingResult process(final FeedSubscription feed) {
        val feedObject = (TRss) feed.rssEntry();
        val storedEntries = storage.getEntries(feed.settings().id());
        val feedEntries = feedObject.getChannel()
                .getItem()
                .stream()
                .map(item -> {
                    val entry = new FeedEntry(item, true);
                    if (!storedEntries.contains(entry)) {
                        return new FeedEntry(item, notificationService.sendEvent(new Rss20NotificationEvent(item)));
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
        log.debug("Extracted {} entries", feedEntries.size());
        val storeResult = storage.putOrReplaceEntry(new Pair<>(feed.settings().id(), feedEntries));
        return new ProcessingResult(storeResult, feed.settings().id(), feedEntries.size());
    }
}
