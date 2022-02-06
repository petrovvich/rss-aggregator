package it.petrovich.rssprocessor.service;

import it.petrovich.rssprocessor.dto.Pair;
import it.petrovich.rssprocessor.dto.RssType;
import it.petrovich.rssprocessor.service.processor.FeedProcessor;
import it.petrovich.rssprocessor.storage.RssStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssUpdateScheduler {
    private final RequestService requestService;
    private final RssStorage storage;
    private final Map<RssType, FeedProcessor> processors;

    @Scheduled(cron = "${rss.update.cron}")
    public void updateRss() {
        storage
                .getAll()
                .stream()
                .map(feed -> {
                    var response = requestService.getRss(feed.url());
                    log.debug("Response with length {} has received", response.length());
                    storage.putSubscription(new Pair<>(feed, response));
                    return storage.getSubscription(feed.id());
                })
                .flatMap(Optional::stream)
                .map(feedSubscription -> ofNullable(processors.get(feedSubscription))
                        .map(processor -> processor.process(feedSubscription))
                        )
                .toList();
    }
}
