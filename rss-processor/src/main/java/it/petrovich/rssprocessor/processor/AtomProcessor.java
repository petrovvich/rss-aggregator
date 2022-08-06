package it.petrovich.rssprocessor.processor;

import it.petrovich.rss.domain.FeedEntry;
import it.petrovich.rss.domain.ProcessingResult;
import it.petrovich.rss.domain.Rss;
import it.petrovich.rss.domain.refactoring.RssType;
import it.petrovich.rss.notification.NotificationProvider;
import it.petrovich.rss.notification.events.AtomNotificationEvent;
import it.petrovich.rss.xml.atom.FeedType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static it.petrovich.rss.domain.XmlUtils.extractEntries;
import static it.petrovich.rss.domain.refactoring.RssType.ATOM;

@Slf4j
@RequiredArgsConstructor
public final class AtomProcessor implements FeedProcessor {
    private static final long ZERO_PROCESSED = 0;
    private final NotificationProvider provider;

    @Override
    public RssType getType() {
        return ATOM;
    }

    @Override
    public ProcessingResult process(final Rss feed) {
        val feedType = (FeedType) feed.convert(feed.renew());
        val toStore = extractEntries(feedType).stream()
                .map(item -> {
                    val entry = new FeedEntry(item, true);
                    if (!feed.contains(entry)) {
                        val feedEntry = new FeedEntry(item, provider.send(new AtomNotificationEvent(UUID.randomUUID(),
                                OffsetDateTime.now(), item)));
                        feed.addItem(feedEntry);
                        return feedEntry;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
        log.debug("Stored {} entries", toStore.size());
        if (!CollectionUtils.isEmpty(toStore)) {
            return new ProcessingResult(true, feed.getId(), toStore.size());
        }
        return new ProcessingResult(true, feed.getId(), ZERO_PROCESSED);
    }
}
