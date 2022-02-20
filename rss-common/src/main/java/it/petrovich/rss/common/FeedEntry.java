package it.petrovich.rss.common;

import java.io.Serializable;
import java.util.UUID;

public record FeedEntry(UUID id, Object entry, boolean isNotified) implements Serializable {
}
