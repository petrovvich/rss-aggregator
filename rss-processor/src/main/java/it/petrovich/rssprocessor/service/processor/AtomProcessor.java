package it.petrovich.rssprocessor.service.processor;

import it.petrovich.rssprocessor.dto.FeedSubscription;
import it.petrovich.rssprocessor.dto.ProcessingResult;
import it.petrovich.rssprocessor.dto.RssType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static it.petrovich.rssprocessor.dto.RssType.ATOM;

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
