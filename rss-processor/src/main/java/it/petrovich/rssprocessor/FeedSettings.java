package it.petrovich.rssprocessor;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record FeedSettings(@NotEmpty String name,
                           @NotBlankUrl String url,
                           @Min(100) @JsonProperty("refresh") long refreshInterval,
                           @NotNull RssType type) {

}