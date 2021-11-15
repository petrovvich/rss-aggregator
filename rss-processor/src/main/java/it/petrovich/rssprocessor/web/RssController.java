package it.petrovich.rssprocessor.web;

import it.petrovich.rssprocessor.dto.FeedResponse;
import it.petrovich.rssprocessor.dto.FeedSettingsRequest;
import it.petrovich.rssprocessor.service.RssService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static it.petrovich.rssprocessor.service.ValidationService.validate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/rss",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RssController {
    private final RssService service;

    @PostMapping
    public FeedResponse saveSettings(@RequestBody @Valid FeedSettingsRequest settings) {
        log.debug("Start process request {}", settings);
        validate(settings);

        return service.save(settings);
    }
}
