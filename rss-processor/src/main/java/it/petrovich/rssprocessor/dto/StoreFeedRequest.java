package it.petrovich.rssprocessor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.petrovich.rss.validation.NotBlankUrl;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import javax.annotation.Nullable;

public record StoreFeedRequest(@NotEmpty String name,
                               @NotBlankUrl String url,
                               @Min(100) @JsonProperty("refresh") long refreshInterval,
                               @Nullable RssType type) {

}