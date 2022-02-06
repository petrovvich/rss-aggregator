package it.petrovich.rssprocessor.service.processor;

import it.petrovich.rssprocessor.dto.FeedSubscription;
import it.petrovich.rssprocessor.dto.ProcessingResult;
import it.petrovich.rssprocessor.dto.RssType;

public sealed interface FeedProcessor permits Rss20Processor, AtomProcessor {

    RssType getType();

    ProcessingResult process(FeedSubscription feed);
}
