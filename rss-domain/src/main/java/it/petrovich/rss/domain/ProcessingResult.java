package it.petrovich.rss.domain;

import java.util.UUID;

public record ProcessingResult(boolean result, UUID rssId, long countProcessed) {
}
