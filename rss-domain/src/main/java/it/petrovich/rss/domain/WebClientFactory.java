package it.petrovich.rss.domain;

import java.net.http.HttpClient;

public class WebClientFactory {

    private static final HttpClient WEB_CLIENT = HttpClient.newHttpClient();

    public static HttpClient webClient() {
        return WEB_CLIENT;
    }
}
