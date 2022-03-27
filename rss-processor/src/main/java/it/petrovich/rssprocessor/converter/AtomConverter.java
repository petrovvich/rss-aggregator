package it.petrovich.rssprocessor.converter;

import it.petrovich.rss.common.Feed;
import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.RssType;
import it.petrovich.rss.xml.XmlUtils;
import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rssprocessor.error.ElementNotFoundException;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;

import static it.petrovich.rss.xml.XmlUtils.DATE_TIME_ELEM_CLASS;
import static it.petrovich.rss.xml.XmlUtils.extractEntry;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
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
    public FeedSubscription convert(final Pair<Feed, String> response) {
        val feed = (FeedType) unmarshall(response.right()).getValue();

        val lastUpdateDate = extractEntry(feed, DATE_TIME_ELEM_CLASS)
                .map(XmlUtils::parseDate)
                .orElseThrow(() -> new ElementNotFoundException(response.left().id()));
        return new FeedSubscription(lastUpdateDate, response.left(), feed);
    }

    @SneakyThrows(value = {JAXBException.class})
    private JAXBElement<?> unmarshall(@NotNull final String source) {
        val unmarshaller = jaxbCtx.createUnmarshaller();
        val inputStream = new ByteArrayInputStream(source.getBytes(UTF_8));
        val streamSource = new StreamSource(inputStream);
        return unmarshaller.unmarshal(streamSource, FeedType.class);
    }
}
