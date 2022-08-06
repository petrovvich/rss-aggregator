package it.petrovich.rssprocessor.service;

import it.petrovich.rss.domain.ProcessingResult;
import it.petrovich.rss.domain.Rss;
import it.petrovich.rss.domain.error.NoFeedException;
import it.petrovich.rss.domain.refactoring.RssType;
import it.petrovich.rss.domain.refactoring.StoreFeedRequest;
import it.petrovich.rss.domain.refactoring.StoreFeedResponse;
import it.petrovich.rss.storage.RssStorage;
import it.petrovich.rssprocessor.processor.FeedProcessor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static it.petrovich.rss.domain.RegistrationStatus.SUCCESS;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
public record SubscriptionService(RssStorage storage,
                                  Map<RssType, FeedProcessor> processors) {

    public StoreFeedResponse save(final Rss rss) {
        log.debug("Start process feed request {}", rss);
        return storage
                .put(rss)
                .map(feed -> new StoreFeedResponse(feed.getId(), SUCCESS, "Subscription has stored successfully"))
                .orElseThrow(() -> new NoFeedException(rss));
    }

    public StoreFeedResponse save(final StoreFeedRequest storeFeedRequest) {
        log.debug("Start process feed request {}", storeFeedRequest);
        return storage
                .put(Rss.fromRequest(storeFeedRequest))
                .map(feed -> new StoreFeedResponse(feed.getId(), SUCCESS, "Subscription has stored successfully"))
                .orElseThrow(() -> new NoFeedException(storeFeedRequest));
    }

    @Scheduled(cron = "${rss.process.cron}")
    public void processRss() {
        val processed = storage.getAllSubscriptions()
                .stream()
                .map(this::process)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        log.debug("Entries have processed {} entries is {}", processed.size(), processed);
    }

    private Optional<ProcessingResult> process(final Rss rss) {
        log.debug("Try to proceed subscription {}", rss);
        return ofNullable(processors.get(rss.getType()))
                .map(processor -> processor.process(rss));
    }
}
