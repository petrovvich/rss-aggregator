package it.petrovich.rss.requester;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
public record SyncHttpRequestService(WebClient webClient) implements RequestService {

    @Override
    @SneakyThrows(value = {URISyntaxException.class})
    public String getRss(final URL url) {
        val uri = url.toURI();

        val response = webClient
                .get()
                .uri(uri)
                .exchangeToMono(rawResponse -> rawResponse.bodyToMono(String.class))
                .block();
        log.debug("Response from {} has received {}", url, response);
        return response;
    }
}
