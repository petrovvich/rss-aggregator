package it.petrovich.rssprocessor.storage;

import it.petrovich.rssprocessor.dto.Feed;
import it.petrovich.rssprocessor.dto.Pair;
import it.petrovich.rssprocessor.dto.StoreFeedRequest;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public sealed interface RssStorage permits InMemoryRssStorage {
    Optional<Feed> put(@NotNull StoreFeedRequest request);

    void put(@NotNull Pair<Feed, String> response);

    Optional<Feed> get(@NotNull UUID id);

    Collection<Feed> getAll();
}
