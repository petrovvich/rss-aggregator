package it.petrovich.rss.domain;

import it.petrovich.rss.domain.conversion.ConversionRequest;
import it.petrovich.rss.domain.conversion.ConversionResponse;
import it.petrovich.rss.domain.conversion.converter.ConverterFactory;
import it.petrovich.rss.domain.conversion.converter.RssConverter;
import it.petrovich.rss.domain.error.ElementNotFoundException;
import it.petrovich.rss.domain.error.UnknownRssTypeException;
import it.petrovich.rss.domain.refactoring.RssType;
import it.petrovich.rss.domain.refactoring.StoreFeedRequest;
import it.petrovich.rss.xml.XmlConfiguration;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Getter
public final class Rss implements Serializable {
    private static final String FEED_TYPE = "FeedType";
    private static final String T_RSS = "TRss";

    private final JAXBContext jaxbContext;
    private final HttpClient webClient;
    private final Collection<RssConverter> converters;

    private final String name;
    private final URL url;
    private final long refreshInterval;
    private final RssType type;
    private final UUID id;
    private LocalDateTime lastUpdate;
    private Object rssEntry;

    private final Collection<FeedEntry> rssItems = new ArrayList<>();


    @SneakyThrows
    private Rss(@NotNull final StoreFeedRequest request, @NotNull final UUID subscriptionId) {
        this.id = subscriptionId;

        this.jaxbContext = XmlConfiguration.getJaxbCtx();
        this.webClient = WebClientFactory.webClient();
        this.converters = ConverterFactory.converters();

        this.name = request.name();
        this.url = request.uri().toURL();
        this.refreshInterval = request.refreshInterval();

        final var rawRss = getRss(this.url);
        if (request.type() == null) {
            this.type = resolve(rawRss);
        } else {
            this.type = request.type();
        }
        final var conversionResponse = internalConvert(rawRss);
        this.lastUpdate = conversionResponse.lastUpdate();
        this.rssEntry = conversionResponse.rssEntry();
    }

    public static Rss fromRequest(@NotNull final StoreFeedRequest request, @NotNull final UUID subscriptionId) {
        return new Rss(request, subscriptionId);
    }

    @SneakyThrows(value = {URISyntaxException.class, IOException.class, InterruptedException.class})
    private String getRss(final URL url) {
        final var httpRequest = HttpRequest.newBuilder().uri(url.toURI()).GET().build();

        return webClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
    }

    public RssType resolve(final String sourceXml) {
        final var feed = unmarshall(sourceXml);
        final var targetClass = feed.getValue().getClass().getSimpleName();

        return switch (targetClass) {
            case T_RSS -> RssType.RSS20;
            case FEED_TYPE -> RssType.ATOM;
            default -> throw new UnknownRssTypeException(targetClass);
        };
    }

    @SneakyThrows(value = {JAXBException.class})
    private JAXBElement<?> unmarshall(@NotNull final String source) {
        final var unmarshaller = jaxbContext.createUnmarshaller();
        final var inputStream = new ByteArrayInputStream(source.getBytes(UTF_8));
        final var streamSource = new StreamSource(inputStream);

        return (JAXBElement<?>) unmarshaller.unmarshal(streamSource);
    }

    private ConversionResponse internalConvert(final String rawRss) {
        return converters.stream()
                .filter(converter -> converter.isApplicable(type))
                .map(converter -> converter.convert(new ConversionRequest(id, rawRss)))
                .findAny()
                .orElseThrow(() -> new ElementNotFoundException(id));
    }

    public String renew() {
        return getRss(this.url);
    }

    public Object convert(final String rawRss) {
        return internalConvert(rawRss).rssEntry();
    }

    public boolean hasItems() {
        return rssItems.isEmpty();
    }

    public void addItem(final FeedEntry item) {
        rssItems.add(item);
    }

    public void addItems(final Collection<FeedEntry> items) {
        rssItems.addAll(items);
    }

    public boolean contains(final FeedEntry item) {
        return rssItems.contains(item);
    }
}
