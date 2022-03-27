package it.petrovich.rssprocessor.converter;

import it.petrovich.rss.common.Feed;
import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.RssType;

public sealed interface RssConverter permits Rss20Converter, AtomConverter {

    RssType getType();

    boolean isApplicable(RssType type);

    FeedSubscription convert(Pair<Feed, String> response);
}
