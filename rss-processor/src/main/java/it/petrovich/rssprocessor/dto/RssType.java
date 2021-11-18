package it.petrovich.rssprocessor.dto;

import lombok.Getter;

/**
 * Supported RSS types.
 * At the moment supports only RSS 2.0 and Atom (this covers almost all site's rss)
 */
@Getter
public enum RssType {
    /**
     * @see <a href="https://validator.w3.org/feed/docs/rss2.html">RSS 2.0 specification</a>
     */
    RSS20("rss20"),
    /**
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4287">Atom specification</a>
     */
    ATOM("atom");

    /**
     * Human-readable representation of the supported RSS format.
     * Uses only inside the app.
     */
    private final String version;

    RssType(final String version) {
        this.version = version;
    }
}
