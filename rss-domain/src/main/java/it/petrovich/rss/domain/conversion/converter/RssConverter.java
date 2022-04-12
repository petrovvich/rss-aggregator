package it.petrovich.rss.domain.conversion.converter;

import it.petrovich.rss.domain.conversion.ConversionRequest;
import it.petrovich.rss.domain.conversion.ConversionResponse;
import it.petrovich.rss.domain.storing.RssType;

public sealed interface RssConverter permits Rss20Converter, AtomConverter {

    RssType getType();

    boolean isApplicable(RssType type);

    ConversionResponse convert(ConversionRequest response);
}
