package it.petrovich.rss.domain.conversion.converter;

import it.petrovich.rss.domain.XmlUtils;
import it.petrovich.rss.domain.conversion.ConversionRequest;
import it.petrovich.rss.domain.conversion.ConversionResponse;
import it.petrovich.rss.domain.error.ElementNotFoundException;
import it.petrovich.rss.domain.refactoring.RssType;
import it.petrovich.rss.xml.rss20111.TRss;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;

import static it.petrovich.rss.domain.XmlUtils.extractEntry;
import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
public final class Rss20Converter implements RssConverter {
    private final RssType rss20Type = RssType.RSS20;
    private final JAXBContext jaxbCtx;

    private static final String LAST_BUILD_DATE = "lastBuildDate";

    @Override
    public RssType getType() {
        return rss20Type;
    }

    @Override
    public boolean isApplicable(final RssType feedType) {
        return rss20Type == feedType;
    }

    @Override
    public ConversionResponse convert(final ConversionRequest request) {
        final var feed = (TRss) unmarshall(request.rawRss()).getValue();

        final var lastUpdateDate = extractEntry(feed, LAST_BUILD_DATE)
                .map(XmlUtils::parseDate)
                .orElseThrow(() -> new ElementNotFoundException(request.id()));
        return new ConversionResponse(request.id(), lastUpdateDate, feed);
    }

    @SneakyThrows(value = {JAXBException.class})
    private JAXBElement<?> unmarshall(@NotNull final String source) {
        final var unmarshaller = jaxbCtx.createUnmarshaller();
        final var inputStream = new ByteArrayInputStream(source.getBytes(UTF_8));
        final var streamSource = new StreamSource(inputStream);
        return unmarshaller.unmarshal(streamSource, TRss.class);
    }
}
