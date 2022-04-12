package it.petrovich.rss.domain.conversion;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConversionResponse(UUID id, LocalDateTime lastUpdate, Object rssEntry) implements Serializable {
}
