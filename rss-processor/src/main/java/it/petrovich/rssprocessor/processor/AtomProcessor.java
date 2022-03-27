package it.petrovich.rssprocessor.processor;

import it.petrovich.rss.common.FeedEntry;
import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.ProcessingResult;
import it.petrovich.rss.common.RssType;
import it.petrovich.rss.notification.NotificationProvider;
import it.petrovich.rss.notification.events.AtomNotificationEvent;
import it.petrovich.rss.storage.RssStorage;
import it.petrovich.rss.xml.atom.FeedType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.UUID;

import static it.petrovich.rss.common.RssType.ATOM;
import static it.petrovich.rss.xml.XmlUtils.extractEntries;

@Slf4j
@Component
@RequiredArgsConstructor
public final class AtomProcessor implements FeedProcessor {
    private static final long ZERO_PROCESSED = 0;
    private final NotificationProvider provider;
    private final RssStorage storage;

    @Override
    public RssType getType() {
        return ATOM;
    }

    @Override
    public ProcessingResult process(final FeedSubscription feed) {
        val feedType = (FeedType) feed.rssEntry();
        val stored = storage.getEntries(feed.settings().id());
        val toStore = extractEntries(feedType).stream()
                .map(item -> {
                    val entry = new FeedEntry(item, true);
                    if (!stored.contains(entry)) {
                        return new FeedEntry(item, provider.send(new AtomNotificationEvent(UUID.randomUUID(), item)));
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
        log.debug("Stored {} entries", toStore.size());
        if (!CollectionUtils.isEmpty(toStore)) {
            val storeResult = storage.putOrReplaceEntry(new Pair<>(feed.settings().id(), toStore));
            return new ProcessingResult(storeResult, feed.settings().id(), toStore.size());
        }
        return new ProcessingResult(true, feed.settings().id(), ZERO_PROCESSED);
    }
}
