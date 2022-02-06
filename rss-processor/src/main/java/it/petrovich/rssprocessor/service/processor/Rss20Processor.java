package it.petrovich.rssprocessor.service.processor;

import it.petrovich.rssprocessor.dto.FeedSubscription;
import it.petrovich.rssprocessor.dto.ProcessingResult;
import it.petrovich.rssprocessor.dto.RssType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static it.petrovich.rssprocessor.dto.RssType.RSS20;

@Slf4j
@Component
@RequiredArgsConstructor
public final class Rss20Processor implements FeedProcessor {

    @Override
    public RssType getType() {
        return RSS20;
    }

    @Override
    public ProcessingResult process(final FeedSubscription feed) {
        return null;
    }
}
