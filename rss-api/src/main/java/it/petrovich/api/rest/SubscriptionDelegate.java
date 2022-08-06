package it.petrovich.api.rest;

import it.petrovich.api.model.SubscriptionRequest;
import it.petrovich.api.model.SubscriptionResponse;
import it.petrovich.rssprocessor.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionDelegate implements SubscriptionApiDelegate {

    private final ApiToDomainMapper mapper = ApiToDomainMapper.INSTANCE;
    private final HttpServletRequest httpServletRequest;
    private final SubscriptionService processor;


    @Override
    public ResponseEntity<SubscriptionResponse> subscriptionPost(final SubscriptionRequest subscriptionRequest) {
        log.debug("Start process subscription request {}", subscriptionRequest);
        final var domainRequest = mapper.map(subscriptionRequest);
        final var domainResponse = processor.save(domainRequest);
        final var apiResponse = mapper.map(domainResponse);

        log.debug("Subscription stored response is {}", apiResponse);
        return ResponseEntity
                .created(URI.create(httpServletRequest.getRequestURI()))
                .body(apiResponse);
    }
}
