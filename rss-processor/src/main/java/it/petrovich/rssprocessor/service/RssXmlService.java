package it.petrovich.rssprocessor.service;

import it.petrovich.rss.common.Feed;
import it.petrovich.rss.common.FeedSubscription;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.RssType;
import it.petrovich.rssprocessor.converter.RssConverter;
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

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.util.Collection;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;

@Slf4j
@Service
public record RssXmlService(JAXBContext jaxbCtx, Collection<RssConverter> converters) {
    private static final String FEED_TYPE = "FeedType";
    private static final String T_RSS = "TRss";

    public FeedSubscription convert(final Pair<Feed, String> response) {
        val type = ofNullable(response.left().type())
                .orElse(resolve(response.right()));

        return converters.stream()
                .filter(converter -> converter.isApplicable(type))
                .map(converter -> converter.convert(response))
                .findAny()
                .orElseThrow(() -> new ElementNotFoundException(response.left().id()));
    }

    public RssType resolve(final String sourceXml) {
        val feed = unmarshall(sourceXml);
        val targetClass = feed.getValue().getClass().getSimpleName();

        return switch (targetClass) {
            case T_RSS -> RssType.RSS20;
            case FEED_TYPE -> RssType.ATOM;
            default -> throw new UnknownRssTypeException(targetClass);
        };
    }

    @SneakyThrows(value = {JAXBException.class})
    private JAXBElement<?> unmarshall(@NotNull final String source) {
        val unmarshaller = jaxbCtx.createUnmarshaller();
        val inputStream = new ByteArrayInputStream(source.getBytes(UTF_8));
        val streamSource = new StreamSource(inputStream);

        return (JAXBElement<?>) unmarshaller.unmarshal(streamSource);
    }
}
