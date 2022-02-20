package it.petrovich.rss.common;

import java.util.UUID;

public record ProcessingResult(boolean result, UUID rssId, long countProcessed) {
}
