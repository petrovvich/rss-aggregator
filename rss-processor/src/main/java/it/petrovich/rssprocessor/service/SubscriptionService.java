package it.petrovich.rssprocessor.service;

import it.petrovich.rss.common.StoreFeedRequest;
import it.petrovich.rss.common.StoreFeedResponse;
import it.petrovich.rss.storage.RssStorage;
import it.petrovich.rssprocessor.error.NoFeedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.petrovich.rss.common.RegistrationStatus.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final RssStorage storage;

    public StoreFeedResponse save(final StoreFeedRequest settings) {
        log.debug("Start process feed request {}", settings);
        return storage
                .putRequest(settings)
                .map(feed -> new StoreFeedResponse(feed.id(), SUCCESS, "Subscription has stored successfully"))
                .orElseThrow(() -> new NoFeedException(settings));
    }
}
