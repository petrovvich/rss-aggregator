package it.petrovich.rssprocessor.storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import it.petrovich.rssprocessor.dto.Feed;
import it.petrovich.rssprocessor.dto.FeedEntry;
import it.petrovich.rssprocessor.dto.FeedSubscription;
import it.petrovich.rssprocessor.dto.Pair;
import it.petrovich.rssprocessor.dto.RssType;
import it.petrovich.rssprocessor.dto.StoreFeedRequest;
import it.petrovich.rssprocessor.service.RequestService;
import it.petrovich.rssprocessor.service.RssXmlService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.openhft.chronicle.map.ChronicleMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static it.petrovich.rssprocessor.XmlUtils.parse;
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
    private final ChronicleMap<UUID, Collection<FeedEntry>> entriesCache = ChronicleMap
            .of(UUID.class, (Class<Collection<FeedEntry>>) (Class) Collection.class)
            .name("feed-notifiers-storage")
            .entries(CACHE_CAPACITY)
            .averageKey(UUID.randomUUID())
            .averageValue(Collections.emptyList())
            .constantKeySizeBySample(UUID.randomUUID())
            .constantValueSizeBySample(Collections.emptyList())
            .averageValueSize(20.0)
            .create();

    private final RssXmlService xmlService;
    private final RequestService requestService;

    @Override
    public Optional<Feed> putRequest(@NotNull final StoreFeedRequest storeFeedRequest) {
        log.debug("Start save feed {}", storeFeedRequest);
        return ofNullable(storeFeedRequest)
                .map(request -> new Feed(UUID.randomUUID(), request.name(), parse(request.url()),
                        request.refreshInterval(), getType(request)))
                .map(request -> {
                    requestsCache.put(request.id(), request);
                    return request;
                });
    }

    private RssType getType(final StoreFeedRequest req) {
        return ofNullable(req.type())
                .orElseGet(() -> xmlService.resolve(requestService.getRss(parse(req.url()))));
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
    public boolean putEntry(final Pair<UUID, Collection<FeedEntry>> entries) {
        if (CollectionUtils.isEmpty(entries.right())) {
            return false;
        }
        entriesCache.put(entries.left(), entries.right());
        return !CollectionUtils.isEmpty(entriesCache.get(entries.left()));
    }

    @Override
    public boolean containsEntry(final Pair<UUID, FeedEntry> entry) {
        val storedEntries = entriesCache.get(entry.left());
        return !CollectionUtils.isEmpty(storedEntries) && storedEntries.contains(entry.right());
    }

    @Override
    public Collection<FeedEntry> getEntries(final UUID id) {
        return ofNullable(entriesCache.get(id))
                .orElse(EMPTY_LIST);
    }
}
