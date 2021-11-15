package it.petrovich.rssprocessor.dto;

import lombok.Getter;

@Getter
public enum RssType {
    RSS20("rss20"),
    ATOM("atom");

    private final String version;

    RssType(String version) {
        this.version = version;
    }
}
