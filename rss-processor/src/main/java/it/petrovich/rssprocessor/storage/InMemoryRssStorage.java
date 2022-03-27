package it.petrovich.rssprocessor.storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import it.petrovich.rss.common.Feed;
import it.petrovich.rss.common.FeedEntry;
import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.RssType;
import it.petrovich.rss.common.StoreFeedRequest;
import it.petrovich.rss.storage.RssStorage;
import it.petrovich.rss.xml.XmlUtils;
import it.petrovich.rssprocessor.service.RequestService;
import it.petrovich.rssprocessor.service.RssXmlService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
@Profile(value = "!prod")
@RequiredArgsConstructor
public final class InMemoryRssStorage implements RssStorage {
    private static final List<FeedEntry> EMPTY_LIST = Collections.emptyList();
    private static final int CACHE_CAPACITY = 1000;

    private final Cache<UUID, Feed> requestsCache = CacheBuilder.newBuilder().maximumSize(CACHE_CAPACITY).build();
    private final Cache<UUID, FeedSubscription> subscriptionsCache = CacheBuilder.newBuilder()
            .maximumSize(CACHE_CAPACITY).build();
    private final Cache<UUID, Collection<FeedEntry>> entriesCache = CacheBuilder.newBuilder()
            .maximumSize(CACHE_CAPACITY).build();

    private final RssXmlService xmlService;
    private final RequestService requestService;

    @Override
    public Optional<Feed> putRequest(@NotNull final StoreFeedRequest storeFeedRequest) {
        log.debug("Start save feed {}", storeFeedRequest);
        return ofNullable(storeFeedRequest)
                .map(request -> new Feed(UUID.randomUUID(), request.name(), XmlUtils.parse(request.url()),
                        request.refreshInterval(), getType(request)))
                .map(request -> {
                    requestsCache.put(request.id(), request);
                    return request;
                });
    }

    private RssType getType(final StoreFeedRequest req) {
        return ofNullable(req.type())
                .orElseGet(() -> xmlService.resolve(requestService.getRss(XmlUtils.parse(req.url()))));
    }

    @Override
    public Optional<Feed> getRequest(final UUID feedId) {
        log.debug("Start search feed with id {}", feedId);
        return ofNullable(requestsCache.getIfPresent(feedId));
    }

    @Override
    public Collection<Feed> getAllRequests() {
        log.debug("Start get all subscriptions");
        return requestsCache
                .asMap()
                .values();
    }

    @Override
    public Optional<FeedSubscription> putSubscription(final Pair<Feed, String> response) {
        subscriptionsCache.put(response.left().id(), xmlService.convert(response));
        requestsCache.invalidate(response.left().id());
        return ofNullable(subscriptionsCache.getIfPresent(response.left().id()));
    }

    @Override
    public Optional<FeedSubscription> getSubscription(final UUID id) {
        return ofNullable(subscriptionsCache.getIfPresent(id));
    }

    @Override
    public Collection<FeedSubscription> getAllSubscriptions() {
        return subscriptionsCache.asMap().values();
    }

    @Override
    public boolean putOrReplaceEntry(final Pair<UUID, Collection<FeedEntry>> entries) {
        if (CollectionUtils.isEmpty(entries.right())) {
            return false;
        }
        entriesCache.put(entries.left(), entries.right());

        return !CollectionUtils.isEmpty(entriesCache.getIfPresent(entries.left()));
    }

    @Override
    public boolean containsEntry(final Pair<UUID, FeedEntry> entry) {
        val storedEntries = entriesCache.getIfPresent(entry.left());
        return !CollectionUtils.isEmpty(storedEntries) && storedEntries.contains(entry.right());
    }

    @Override
    public Collection<FeedEntry> getEntries(final UUID id) {
        return ofNullable(entriesCache.getIfPresent(id))
                .orElse(EMPTY_LIST);
    }
}
