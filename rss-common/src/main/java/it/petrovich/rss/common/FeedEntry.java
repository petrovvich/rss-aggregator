package it.petrovich.rss.common;

import java.io.Serializable;

public record FeedEntry(Object entry, boolean isNotified) implements Serializable {
}
