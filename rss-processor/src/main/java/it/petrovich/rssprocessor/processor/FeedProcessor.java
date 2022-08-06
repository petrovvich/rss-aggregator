package it.petrovich.rssprocessor.processor;

import it.petrovich.rss.domain.ProcessingResult;
import it.petrovich.rss.domain.Rss;
import it.petrovich.rss.domain.refactoring.RssType;

public sealed interface FeedProcessor permits Rss20Processor, AtomProcessor {

    RssType getType();

    ProcessingResult process(Rss feed);
}
