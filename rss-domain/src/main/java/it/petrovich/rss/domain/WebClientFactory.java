package it.petrovich.rss.domain;

import org.springframework.web.reactive.function.client.WebClient;

public class WebClientFactory {

    private static final WebClient WEB_CLIENT = initClient();

    private static WebClient initClient() {
        return WebClient.builder().build();
    }

    public static WebClient webClient() {
        return WEB_CLIENT;
    }
}
