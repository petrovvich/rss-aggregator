package it.petrovich.rss.domain;

import java.io.Serializable;

public record FeedEntry(Object entry, boolean isNotified) implements Serializable {
}
