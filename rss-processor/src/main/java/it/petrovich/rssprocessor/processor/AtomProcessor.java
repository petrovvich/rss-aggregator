package it.petrovich.rssprocessor.processor;

import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.ProcessingResult;
import it.petrovich.rss.common.RssType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static it.petrovich.rss.common.RssType.ATOM;

@Slf4j
@Component
@RequiredArgsConstructor
public final class AtomProcessor implements FeedProcessor {

    @Override
    public RssType getType() {
        return ATOM;
    }

    @Override
    public ProcessingResult process(final FeedSubscription feed) {
        return null;
    }
}
