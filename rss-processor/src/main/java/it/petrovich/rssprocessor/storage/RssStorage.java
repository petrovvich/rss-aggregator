package it.petrovich.rssprocessor.storage;

import it.petrovich.rssprocessor.dto.Feed;
import it.petrovich.rssprocessor.dto.FeedEntry;
import it.petrovich.rssprocessor.dto.FeedSubscription;
import it.petrovich.rssprocessor.dto.Pair;
import it.petrovich.rssprocessor.dto.StoreFeedRequest;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public sealed interface RssStorage permits InMemoryRssStorage {
    Optional<Feed> putRequest(@NotNull StoreFeedRequest request);

    Optional<Feed> getRequest(@NotNull UUID id);

    Collection<Feed> getAllRequests();

    Optional<FeedSubscription> putSubscription(@NotNull Pair<Feed, String> response);

    Optional<FeedSubscription> getSubscription(@NotNull UUID id);

    Collection<FeedSubscription> getAllSubscriptions();

    boolean putEntry(@NotNull Pair<UUID, Collection<FeedEntry>> entries);

    boolean containsEntry(Pair<UUID, FeedEntry> entry);

    Collection<FeedEntry> getEntries(UUID id);
}
