package it.petrovich.rss.domain.conversion.converter;

import it.petrovich.rss.xml.XmlConfiguration;

import java.util.Collection;
import java.util.List;

public class ConverterFactory {
    private static final Collection<RssConverter> RSS_CONVERTERS = List.of(
            new AtomConverter(XmlConfiguration.getJaxbCtx()),
            new Rss20Converter(XmlConfiguration.getJaxbCtx()));

    public static Collection<RssConverter> converters() {
        return RSS_CONVERTERS;
    }
}
