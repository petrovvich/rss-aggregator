package it.petrovich.rssprocessor.storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import it.petrovich.rssprocessor.dto.Feed;
import it.petrovich.rssprocessor.dto.FeedSubscription;
import it.petrovich.rssprocessor.dto.Pair;
import it.petrovich.rssprocessor.dto.RssType;
import it.petrovich.rssprocessor.dto.StoreFeedRequest;
import it.petrovich.rssprocessor.service.RequestService;
import it.petrovich.rssprocessor.service.RssXmlService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static it.petrovich.rssprocessor.XmlUtils.parse;
import static java.util.Optional.ofNullable;

@Slf4j
@Component
@Profile(value = "!prod")
@RequiredArgsConstructor
public final class InMemoryRssStorage implements RssStorage {
    private static final int CACHE_CAPACITY = 1000;
    private final Cache<UUID, Feed> requests = CacheBuilder.newBuilder().maximumSize(CACHE_CAPACITY).build();
    private final Cache<UUID, FeedSubscription> subscriptions = CacheBuilder.newBuilder().maximumSize(CACHE_CAPACITY)
            .build();

    private final RssXmlService xmlService;
    private final RequestService requestService;

    @Override
    public Optional<Feed> putRequest(@NotNull final StoreFeedRequest storeFeedRequest) {
        log.debug("Start save feed {}", storeFeedRequest);
        return ofNullable(storeFeedRequest)
                .map(request -> new Feed(UUID.randomUUID(), request.name(), getUrl(request.url()),
                        request.refreshInterval(), getType(request)))
                .map(request -> {
                    requests.put(request.id(), request);
                    return request;
                });
    }

    private RssType getType(final StoreFeedRequest req) {
        return ofNullable(req.type())
                .orElseGet(() -> xmlService.resolve(requestService.getRss(parse(req.url()))));
    }

    @SneakyThrows(value = {MalformedURLException.class})
    private URL getUrl(final String source) {
        return new URL(source);
    }

    @Override
    public Optional<Feed> getRequest(final UUID feedId) {
        log.debug("Start search feed with id {}", feedId);
        return ofNullable(requests.getIfPresent(feedId));
    }

    @Override
    public void putSubscription(final Pair<Feed, String> response) {
        subscriptions.put(response.left().id(), xmlService.convert(response));
    }

    @Override
    public Optional<FeedSubscription> getSubscription(final UUID id) {
        return Optional.empty();
    }

    @Override
    public Collection<Feed> getAll() {
        log.debug("Start get all subscriptions");
        return requests
                .asMap()
                .values();
    }
}
