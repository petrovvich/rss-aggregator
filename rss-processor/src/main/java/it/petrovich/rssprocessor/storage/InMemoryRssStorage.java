package it.petrovich.rssprocessor.storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import it.petrovich.rssprocessor.dto.Feed;
import it.petrovich.rssprocessor.dto.StoreFeedRequest;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@Profile(value = "local")
public final class InMemoryRssStorage implements RssStorage {
    private final Cache<UUID, Feed> CACHE = CacheBuilder.newBuilder().maximumSize(1000).build();

    @Override
    public Optional<Feed> put(@NotNull StoreFeedRequest request) {
        log.debug("Start save feed {}", request);
        return Optional
                .ofNullable(request)
                .map(req -> new Feed(UUID.randomUUID(), req.name(), getUrl(req.url()), req.refreshInterval(), req.type()))
                .map(this::putToStorage);
    }

    @SneakyThrows(value = {MalformedURLException.class})
    private URL getUrl(String source) {
        return new URL(source);
    }

    private Feed putToStorage(Feed feed) {
        CACHE.put(feed.id(), feed);
        return feed;
    }

    @Override
    public Optional<Feed> get(UUID feedId) {
        log.debug("Start search feed with id {}", feedId);
        return Optional
                .ofNullable(CACHE.getIfPresent(feedId));
    }
}
