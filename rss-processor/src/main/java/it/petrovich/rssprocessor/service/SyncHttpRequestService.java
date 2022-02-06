package it.petrovich.rssprocessor.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncHttpRequestService implements RequestService {
    private final RestTemplate restTemplate;

    @Override
    @SneakyThrows(value = {URISyntaxException.class})
    public String getRss(final URL url) {
        return restTemplate.getForObject(url.toURI(), String.class);
    }
}
