package it.petrovich.rssprocessor.service.processor;

import it.petrovich.rss.xml.rss20111.TRss;
import it.petrovich.rssprocessor.dto.FeedSubscription;
import it.petrovich.rssprocessor.dto.ProcessingResult;
import it.petrovich.rssprocessor.dto.RssType;
import it.petrovich.rssprocessor.storage.RssStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import static it.petrovich.rssprocessor.dto.RssType.RSS20;

@Slf4j
@Component
@RequiredArgsConstructor
public final class Rss20Processor implements FeedProcessor {
    private final RssStorage storage;

    @Override
    public RssType getType() {
        return RSS20;
    }

    @Override
    public ProcessingResult process(final FeedSubscription feed) {
        val feedObject = (TRss) feed.rssEntry();

        return null;
    }
}
