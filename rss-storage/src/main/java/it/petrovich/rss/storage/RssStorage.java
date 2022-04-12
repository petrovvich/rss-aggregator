package it.petrovich.rss.storage;

import it.petrovich.rss.domain.Rss;

import java.util.Collection;
import java.util.Optional;

public interface RssStorage {
    Optional<Rss> put(Optional<Rss> request);

    Collection<Rss> getAllSubscriptions();
}
