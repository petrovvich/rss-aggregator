package it.petrovich.rssprocessor.storage;

import it.petrovich.rss.common.Feed;
import it.petrovich.rss.common.FeedEntry;
import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.StoreFeedRequest;
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

    boolean putOrReplaceEntry(@NotNull Pair<UUID, Collection<FeedEntry>> entries);

    boolean containsEntry(Pair<UUID, FeedEntry> entry);

    Collection<FeedEntry> getEntries(UUID id);
}
