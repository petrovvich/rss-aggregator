package it.petrovich.rssprocessor.dto;

import java.time.LocalDateTime;

public record FeedSubscription(LocalDateTime lastUpdate, Feed settings, Object rssEntry) {
}
