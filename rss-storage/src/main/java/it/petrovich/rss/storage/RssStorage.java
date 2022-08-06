package it.petrovich.rss.storage;

import it.petrovich.rss.domain.Pair;
import it.petrovich.rss.domain.Rss;
import it.petrovich.rss.domain.refactoring.StoreFeedRequest;
import it.petrovich.rss.domain.refactoring.StoreFeedResponse;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface RssStorage {
    Optional<Rss> put(Rss request);

    StoreFeedResponse put(StoreFeedRequest request);

    Collection<Rss> getAllSubscriptions();

    Collection<Pair<UUID, StoreFeedRequest>> getAllRequests();
}
