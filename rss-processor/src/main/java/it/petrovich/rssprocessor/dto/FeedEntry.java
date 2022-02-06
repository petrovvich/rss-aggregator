package it.petrovich.rssprocessor.dto;

import java.io.Serializable;

public record FeedEntry(Object entry, boolean isNotified) implements Serializable {
}
