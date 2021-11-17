package it.petrovich.rssprocessor.service;

import it.petrovich.rssprocessor.dto.Pair;
import it.petrovich.rssprocessor.storage.RssStorage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssUpdateService {
    private final RequestService requestService;
    private final RssStorage storage;

    @Scheduled(fixedRate = 15000)
    public void updateRss() {
        storage
                .getAll()
                .forEach(feed -> {
                    var response = requestService.getRss(feed.url());
                    log.debug("Response with length {} has received", response.length());
                    storage.put(new Pair<>(feed, response));
                });
    }
}
