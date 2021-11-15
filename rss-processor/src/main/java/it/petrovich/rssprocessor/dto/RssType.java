package it.petrovich.rssprocessor.dto;

import lombok.Getter;

@Getter
public enum RssType {
    RSS_2_0("rss_2_0"),
    ATOM("atom");

    private final String version;

    RssType(String version) {
        this.version = version;
    }
}
