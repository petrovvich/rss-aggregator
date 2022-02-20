package it.petrovich.rssprocessor.processor;

import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.ProcessingResult;
import it.petrovich.rss.common.RssType;

public sealed interface FeedProcessor permits Rss20Processor, AtomProcessor {

    RssType getType();

    ProcessingResult process(FeedSubscription feed);
}
