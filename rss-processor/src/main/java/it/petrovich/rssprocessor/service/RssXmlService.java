package it.petrovich.rssprocessor.service;

import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import it.petrovich.rssprocessor.XmlUtils;
import it.petrovich.rssprocessor.dto.Feed;
import it.petrovich.rssprocessor.dto.FeedSubscription;
import it.petrovich.rssprocessor.dto.Pair;
import it.petrovich.rssprocessor.error.ElementNotFoundException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;

import static it.petrovich.rssprocessor.XmlUtils.DATE_TIME_ELEM_CLASS;
import static it.petrovich.rssprocessor.XmlUtils.extractEntry;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssXmlService {
    public static final String LAST_BUILD_DATE = "lastBuildDate";
    private final JAXBContext jaxbCtx;

    public FeedSubscription convert(Pair<Feed, String> response) {
        return switch (response.left().type()) {
            case RSS20 -> convertRss20(response);
            case ATOM -> convertAtom(response);
        };
    }

    @SneakyThrows(value = {JAXBException.class})
    private FeedSubscription convertRss20(Pair<Feed, String> response) {
        val unmarshaller = jaxbCtx.createUnmarshaller();
        val feed = (TRss) unmarshaller
                .unmarshal(new StreamSource(new ByteArrayInputStream(response.right().getBytes(UTF_8))), TRss.class)
                .getValue();

        val lastUpdateDate = extractEntry(feed, LAST_BUILD_DATE)
                .map(XmlUtils::parseDate)
                .orElseThrow(() -> new ElementNotFoundException(response.left().id()));
        return new FeedSubscription(lastUpdateDate, response.left(), feed);
    }

    @SneakyThrows(value = {JAXBException.class})
    private FeedSubscription convertAtom(Pair<Feed, String> response) {
        val unmarshaller = jaxbCtx.createUnmarshaller();
        val feed = (FeedType) unmarshaller
                .unmarshal(new StreamSource(new ByteArrayInputStream(response.right().getBytes(UTF_8))), FeedType.class)
                .getValue();

        val lastUpdateDate = extractEntry(feed, DATE_TIME_ELEM_CLASS)
                .map(XmlUtils::parseDate)
                .orElseThrow(() -> new ElementNotFoundException(response.left().id()));
        return new FeedSubscription(lastUpdateDate, response.left(), feed);
    }
}
