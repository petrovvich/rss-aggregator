package it.petrovich.rss.domain.conversion.converter;

import it.petrovich.rss.domain.conversion.ConversionRequest;
import it.petrovich.rss.domain.conversion.ConversionResponse;
import it.petrovich.rss.domain.storing.RssType;
import it.petrovich.rss.domain.error.ElementNotFoundException;
import it.petrovich.rss.domain.XmlUtils;
import it.petrovich.rss.xml.atom.FeedType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;

import static it.petrovich.rss.domain.XmlUtils.DATE_TIME_ELEM_CLASS;
import static it.petrovich.rss.domain.XmlUtils.extractEntry;
import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
public final class AtomConverter implements RssConverter {
    private final RssType atomType = RssType.ATOM;
    private final JAXBContext jaxbCtx;

    @Override
    public RssType getType() {
        return atomType;
    }

    @Override
    public boolean isApplicable(final RssType type) {
        return atomType == type;
    }

    @Override
    public ConversionResponse convert(final ConversionRequest request) {
        val feed = (FeedType) unmarshall(request.rawRss()).getValue();

        val lastUpdateDate = extractEntry(feed, DATE_TIME_ELEM_CLASS)
                .map(XmlUtils::parseDate)
                .orElseThrow(() -> new ElementNotFoundException(request.id()));
        return new ConversionResponse(request.id(), lastUpdateDate, feed);
    }

    @SneakyThrows(value = {JAXBException.class})
    private JAXBElement<?> unmarshall(@NotNull final String source) {
        val unmarshaller = jaxbCtx.createUnmarshaller();
        val inputStream = new ByteArrayInputStream(source.getBytes(UTF_8));
        val streamSource = new StreamSource(inputStream);
        return unmarshaller.unmarshal(streamSource, FeedType.class);
    }
}
