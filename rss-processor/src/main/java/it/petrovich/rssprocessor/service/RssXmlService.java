package it.petrovich.rssprocessor.service;

import it.petrovich.rss.common.Feed;
import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.RssType;
import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import it.petrovich.rssprocessor.XmlUtils;
import it.petrovich.rssprocessor.error.ElementNotFoundException;
import it.petrovich.rssprocessor.error.UnknownRssTypeException;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;

import static it.petrovich.rssprocessor.XmlUtils.DATE_TIME_ELEM_CLASS;
import static it.petrovich.rssprocessor.XmlUtils.extractEntry;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
public record RssXmlService(JAXBContext jaxbCtx) {
    private static final String LAST_BUILD_DATE = "lastBuildDate";
    private static final String T_RSS = "TRss";
    private static final String FEED_TYPE = "FeedType";

    public FeedSubscription convert(final Pair<Feed, String> response) {
        val type = ofNullable(response.left().type())
                .orElse(resolve(response.right()));

        return switch (type) {
            case RSS20 -> convertRss20(response);
            case ATOM -> convertAtom(response);
        };
    }

    private FeedSubscription convertRss20(final Pair<Feed, String> response) {
        val feed = (TRss) unmarshall(response.right(), TRss.class).getValue();

        val lastUpdateDate = extractEntry(feed, LAST_BUILD_DATE)
                .map(XmlUtils::parseDate)
                .orElseThrow(() -> new ElementNotFoundException(response.left().id()));
        return new FeedSubscription(lastUpdateDate, response.left(), feed);
    }

    private FeedSubscription convertAtom(final Pair<Feed, String> response) {
        val feed = (FeedType) unmarshall(response.right(), FeedType.class).getValue();

        val lastUpdateDate = extractEntry(feed, DATE_TIME_ELEM_CLASS)
                .map(XmlUtils::parseDate)
                .orElseThrow(() -> new ElementNotFoundException(response.left().id()));
        return new FeedSubscription(lastUpdateDate, response.left(), feed);
    }

    public RssType resolve(final String sourceXml) {
        val feed = unmarshall(sourceXml, null);
        val targetClass = feed.getValue().getClass().getSimpleName();

        return switch (targetClass) {
            case T_RSS -> RssType.RSS20;
            case FEED_TYPE -> RssType.ATOM;
            default -> throw new UnknownRssTypeException(targetClass);
        };
    }

    @SneakyThrows(value = {JAXBException.class})
    private JAXBElement<?> unmarshall(@NotNull final String source, @Nullable final Class<?> targetClass) {
        val unmarshaller = jaxbCtx.createUnmarshaller();
        return ofNullable(targetClass).isEmpty()
                ? (JAXBElement<?>) unmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(source.getBytes(UTF_8))))
                : unmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(source.getBytes(UTF_8))), targetClass);
    }
}
