package it.petrovich.rss.domain.refactoring;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import javax.annotation.Nullable;
import java.net.URI;

public record StoreFeedRequest(@NotEmpty String name,
                               URI uri,
                               @Min(100) long refreshInterval,
                               @Nullable RssType type) {
}
