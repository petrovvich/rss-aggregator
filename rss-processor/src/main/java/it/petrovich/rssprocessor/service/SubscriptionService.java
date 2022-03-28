package it.petrovich.rssprocessor.service;

import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.ProcessingResult;
import it.petrovich.rss.common.RssType;
import it.petrovich.rss.common.StoreFeedRequest;
import it.petrovich.rss.common.StoreFeedResponse;
import it.petrovich.rss.requester.RequestService;
import it.petrovich.rss.storage.RssStorage;
import it.petrovich.rss.common.error.NoFeedException;
import it.petrovich.rssprocessor.processor.FeedProcessor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static it.petrovich.rss.common.RegistrationStatus.SUCCESS;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
public record SubscriptionService(RequestService requestService,
                                  RssStorage storage,
                                  Map<RssType, FeedProcessor> processors) {

    public StoreFeedResponse save(final StoreFeedRequest settings) {
        log.debug("Start process feed request {}", settings);
        return storage
                .putRequest(settings)
                .map(feed -> new StoreFeedResponse(feed.id(), SUCCESS, "Subscription has stored successfully"))
                .orElseThrow(() -> new NoFeedException(settings));
    }

    @Scheduled(cron = "${rss.update.cron}")
    public void updateRss() {
        val subscriptions = storage
                .getAllRequests()
                .stream()
                .map(feed -> new Pair<>(feed, requestService.getRss(feed.url())))
                .map(storage::putSubscription)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        log.debug("Entries have added {} entries is {}", subscriptions.size(), subscriptions);
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

    private Optional<ProcessingResult> process(final FeedSubscription feedSubscription) {
        log.debug("Try to proceed subscription {}", feedSubscription.settings());
        return ofNullable(processors.get(feedSubscription.settings().type()))
                .map(processor -> processor.process(feedSubscription));
    }
}
