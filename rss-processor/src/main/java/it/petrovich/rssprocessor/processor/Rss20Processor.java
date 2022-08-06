package it.petrovich.rssprocessor.processor;

import it.petrovich.rss.domain.FeedEntry;
import it.petrovich.rss.domain.ProcessingResult;
import it.petrovich.rss.domain.Rss;
import it.petrovich.rss.domain.refactoring.RssType;
import it.petrovich.rss.notification.NotificationProvider;
import it.petrovich.rss.notification.events.Rss20NotificationEvent;
import it.petrovich.rss.xml.rss20111.TRss;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static it.petrovich.rss.domain.refactoring.RssType.RSS20;

@Slf4j
@RequiredArgsConstructor
public final class Rss20Processor implements FeedProcessor {
    private static final int ZERO_PROCESSED = 0;
    private final NotificationProvider provider;

    @Override
    public RssType getType() {
        return RSS20;
    }

    @Override
    public ProcessingResult process(final Rss feed) {
        val feedObject = (TRss) feed.convert(feed.renew());
        val feedEntries = feedObject.getChannel()
                .getItem()
                .stream()
                .map(item -> {
                    val entry = new FeedEntry(item, true);
                    if (!feed.contains(entry)) {
                        val feedEntry = new FeedEntry(item, provider.send(new Rss20NotificationEvent(UUID.randomUUID(),
                                OffsetDateTime.now(), item)));
                        feed.addItem(feedEntry);
                        return feedEntry;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
        log.debug("Extracted {} entries", feedEntries.size());
        if (!CollectionUtils.isEmpty(feedEntries)) {
            return new ProcessingResult(true, feed.getId(), feedEntries.size());
        }
        return new ProcessingResult(true, feed.getId(), ZERO_PROCESSED);
    }
}
