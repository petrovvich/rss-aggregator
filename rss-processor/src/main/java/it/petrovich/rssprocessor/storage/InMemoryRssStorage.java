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

@Slf4j
@Component
@Profile(value = "!prod")
@RequiredArgsConstructor
public final class InMemoryRssStorage implements RssStorage {
    private final Cache<UUID, Feed> REQUESTS = CacheBuilder.newBuilder().maximumSize(1000).build();
    private final Cache<UUID, FeedSubscription> SUBSCRIPTIONS = CacheBuilder.newBuilder().maximumSize(1000).build();

    private final RssXmlService xmlService;
    private final RequestService requestService;

    @Override
    public Optional<Feed> put(@NotNull StoreFeedRequest request) {
        log.debug("Start save feed {}", request);
        return Optional
                .ofNullable(request)
                .map(req -> new Feed(UUID.randomUUID(), req.name(), getUrl(req.url()), req.refreshInterval(), getType(req)))
                .map(this::putToStorage);
    }

    @SneakyThrows
    private RssType getType(StoreFeedRequest req) {
        return Optional
                .ofNullable(req.type())
                .orElseGet(() -> xmlService.resolve(requestService.getRss(parse(req.url()))));
    }

    @Override
    public void put(Pair<Feed, String> response) {
        SUBSCRIPTIONS.put(response.left().id(), xmlService.convert(response));
    }

    @SneakyThrows(value = {MalformedURLException.class})
    private URL getUrl(String source) {
        return new URL(source);
    }

    private Feed putToStorage(Feed feed) {
        REQUESTS.put(feed.id(), feed);
        return feed;
    }

    @Override
    public Optional<Feed> get(UUID feedId) {
        log.debug("Start search feed with id {}", feedId);
        return Optional
                .ofNullable(REQUESTS.getIfPresent(feedId));
    }

    @Override
    public Collection<Feed> getAll() {
        log.debug("Start get all subscriptions");
        return REQUESTS
                .asMap()
                .values();
    }
}
