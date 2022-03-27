package it.petrovich.rss.requester;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RequesterAutoConfigurer {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public RequestService requestService(final WebClient webClient) {
        return new SyncHttpRequestService(webClient);
    }
}
