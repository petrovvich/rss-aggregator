package it.petrovich.rss.domain.conversion;

import java.io.Serializable;
import java.util.UUID;

public record ConversionRequest(UUID id, String rawRss) implements Serializable {
}
