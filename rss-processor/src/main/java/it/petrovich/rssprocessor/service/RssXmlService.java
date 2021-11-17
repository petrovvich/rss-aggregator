package it.petrovich.rssprocessor.service;

import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import it.petrovich.rssprocessor.XmlUtils;
import it.petrovich.rssprocessor.dto.Feed;
import it.petrovich.rssprocessor.dto.FeedSubscription;
import it.petrovich.rssprocessor.dto.Pair;
import it.petrovich.rssprocessor.dto.RssType;
import it.petrovich.rssprocessor.error.ElementNotFoundException;
import it.petrovich.rssprocessor.error.UnknownRssTypeException;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.util.Optional;

import static it.petrovich.rssprocessor.XmlUtils.DATE_TIME_ELEM_CLASS;
import static it.petrovich.rssprocessor.XmlUtils.extractEntry;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssXmlService {
    private static final String LAST_BUILD_DATE = "lastBuildDate";
    private static final String T_RSS = "TRss";
    private static final String FEED_TYPE = "FeedType";
    private final JAXBContext jaxbCtx;

    public FeedSubscription convert(Pair<Feed, String> response) {
        val type = Optional
                .ofNullable(response.left().type())
                .orElse(resolve(response.right()));

        return switch (type) {
            case RSS20 -> convertRss20(response);
            case ATOM -> convertAtom(response);
        };
    }

    private FeedSubscription convertRss20(Pair<Feed, String> response) {
        val feed = (TRss) unmarshall(response.right(), TRss.class).getValue();

        val lastUpdateDate = extractEntry(feed, LAST_BUILD_DATE)
                .map(XmlUtils::parseDate)
                .orElseThrow(() -> new ElementNotFoundException(response.left().id()));
        return new FeedSubscription(lastUpdateDate, response.left(), feed);
    }

    private FeedSubscription convertAtom(Pair<Feed, String> response) {
        val feed = (FeedType) unmarshall(response.right(), FeedType.class).getValue();

        val lastUpdateDate = extractEntry(feed, DATE_TIME_ELEM_CLASS)
                .map(XmlUtils::parseDate)
                .orElseThrow(() -> new ElementNotFoundException(response.left().id()));
        return new FeedSubscription(lastUpdateDate, response.left(), feed);
    }

    public RssType resolve(String sourceXml) {
        val feed = unmarshall(sourceXml, null);
        val targetClass = feed.getValue().getClass().getSimpleName();

        return switch (targetClass) {
            case T_RSS -> RssType.RSS20;
            case FEED_TYPE -> RssType.ATOM;
            default -> throw new UnknownRssTypeException(targetClass);
        };
    }

    @SneakyThrows(value = {JAXBException.class})
    private JAXBElement unmarshall(@NotNull String source, @Nullable Class<?> targetClass) {
        val unmarshaller = jaxbCtx.createUnmarshaller();
        return Optional.ofNullable(targetClass).isEmpty()
                ? (JAXBElement) unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(source.getBytes(UTF_8))))
                : unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(source.getBytes(UTF_8))), targetClass);
    }
}
