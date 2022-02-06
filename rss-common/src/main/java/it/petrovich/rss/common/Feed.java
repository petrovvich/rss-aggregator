package it.petrovich.rss.common;

import it.petrovich.rss.validation.NotBlankUrl;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.net.URL;
import java.util.UUID;

public record Feed(@NotNull UUID id,
                   @NotEmpty String name,
                   @NotBlankUrl URL url,
                   @Min(100) long refreshInterval,
                   @NotNull RssType type) {
}
