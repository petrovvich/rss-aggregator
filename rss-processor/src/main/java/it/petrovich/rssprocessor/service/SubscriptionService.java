package it.petrovich.rssprocessor.service;

import it.petrovich.rss.domain.ProcessingResult;
import it.petrovich.rss.domain.Rss;
import it.petrovich.rss.domain.refactoring.RssType;
import it.petrovich.rss.domain.refactoring.StoreFeedRequest;
import it.petrovich.rss.domain.refactoring.StoreFeedResponse;
import it.petrovich.rss.storage.RssStorage;
import it.petrovich.rssprocessor.processor.FeedProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Slf4j
public record SubscriptionService(RssStorage storage, Map<RssType, FeedProcessor> processors) {

    public StoreFeedResponse save(final StoreFeedRequest storeFeedRequest) {
        log.debug("Start process feed request {}", storeFeedRequest);
        return storage.put(storeFeedRequest);
    }

    @Scheduled(cron = "${rss.create.cron}")
    public void createRss() {
        final var processed = storage.getAllRequests()
                .stream()
                .map(pair -> Rss.fromRequest(pair.right(), pair.left()))
                .toList();
        log.debug("Subscription requests processed {} entries is {}", processed.size(), processed);
    }

    @Scheduled(cron = "${rss.process.cron}")
    public void processRss() {
        final var processed = storage.getAllSubscriptions()
                .stream()
                .map(this::process)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        log.debug("RSS processed {} entries is {}", processed.size(), processed);
    }

    private Optional<ProcessingResult> process(final Rss rss) {
        log.debug("Try to proceed subscription {}", rss);
        return ofNullable(processors.get(rss.getType()))
                .map(processor -> processor.process(rss));
    }
}
