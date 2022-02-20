package it.petrovich.rss.common;

import java.time.LocalDateTime;

public record FeedSubscription(LocalDateTime lastUpdate, Feed settings, Object rssEntry) {
}
