package it.petrovich.api.rest;

import it.petrovich.api.model.SubscriptionRequest;
import it.petrovich.api.model.SubscriptionResponse;
import it.petrovich.rssprocessor.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionDelegate implements SubscriptionApiDelegate {

    private final ApiToDomainMapper mapper = ApiToDomainMapper.INSTANCE;
    private final SubscriptionService subscriptionService;
    private final NativeWebRequest nativeWebRequest;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<SubscriptionResponse> subscriptionPost(final SubscriptionRequest subscriptionRequest) {
        log.debug("Start process subscription request {}", subscriptionRequest);
        final var domainRequest = mapper.map(subscriptionRequest);
        final var domainResponse = subscriptionService.save(domainRequest);
        final var apiResponse = mapper.map(domainResponse);

        log.debug("Subscription stored response is {}", apiResponse);
        return ResponseEntity
                .created(URI.create(nativeWebRequest.getContextPath()))
                .body(apiResponse);
    }
}
