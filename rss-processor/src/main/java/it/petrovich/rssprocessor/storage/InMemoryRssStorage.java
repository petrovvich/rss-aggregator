package it.petrovich.rssprocessor.storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import it.petrovich.rss.domain.Rss;
import it.petrovich.rss.storage.RssStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Profile(value = "!prod")
@RequiredArgsConstructor
public final class InMemoryRssStorage implements RssStorage {
    private static final int CACHE_CAPACITY = 1000;

    private final Cache<UUID, Rss> subscriptionsCache = CacheBuilder.newBuilder().maximumSize(CACHE_CAPACITY).build();

    @Override
    public Optional<Rss> put(final Rss rss) {
        log.debug("Start save feed {}", rss);

        subscriptionsCache.put(rss.getId(), rss);
        return Optional.of(rss);
    }

    @Override
    public Collection<Rss> getAllSubscriptions() {
        return subscriptionsCache.asMap().values();
    }
}
