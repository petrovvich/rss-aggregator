package it.petrovich.rssprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssGrabService {
    private final RestTemplate restTemplate;

    public String grabRss() {
        return restTemplate.getForObject("https://blog.jetbrains.com/feed/", String.class);
    }
}
