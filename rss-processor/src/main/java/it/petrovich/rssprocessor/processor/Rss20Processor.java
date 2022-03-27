package it.petrovich.rssprocessor.processor;

import it.petrovich.rss.common.FeedEntry;
import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.ProcessingResult;
import it.petrovich.rss.common.RssType;
import it.petrovich.rss.notification.NotificationProvider;
import it.petrovich.rss.notification.events.Rss20NotificationEvent;
import it.petrovich.rss.storage.RssStorage;
import it.petrovich.rss.xml.rss20111.TRss;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.UUID;

import static it.petrovich.rss.common.RssType.RSS20;

@Slf4j
@Component
@RequiredArgsConstructor
public final class Rss20Processor implements FeedProcessor {
    private static final int ZERO_PROCESSED = 0;
    private final NotificationProvider provider;
    private final RssStorage storage;

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
                        return new FeedEntry(item, provider.send(new Rss20NotificationEvent(UUID.randomUUID(), item)));
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
        log.debug("Extracted {} entries", feedEntries.size());
        if (!CollectionUtils.isEmpty(feedEntries)) {
            val storeResult = storage.putOrReplaceEntry(new Pair<>(feed.settings().id(), feedEntries));
            return new ProcessingResult(storeResult, feed.settings().id(), feedEntries.size());
        }
        return new ProcessingResult(true, feed.settings().id(), ZERO_PROCESSED);
    }
}
