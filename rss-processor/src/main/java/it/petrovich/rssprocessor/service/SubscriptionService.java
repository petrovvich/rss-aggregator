package it.petrovich.rssprocessor.service;

import it.petrovich.rssprocessor.dto.StoreFeedRequest;
import it.petrovich.rssprocessor.dto.StoreFeedResponse;
import it.petrovich.rssprocessor.error.NoFeedException;
import it.petrovich.rssprocessor.storage.RssStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.petrovich.rssprocessor.dto.RegistrationStatus.SUCCESS;

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
