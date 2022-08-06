package it.petrovich.rss.xml;

import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static it.petrovich.rss.xml.TestUtil.ATOM_JAVAREVISITED_RESPONSE_XML;
import static it.petrovich.rss.xml.TestUtil.buildSource;
import static it.petrovich.rss.xml.TestUtil.initJaxbContext;
import static it.petrovich.rss.xml.TestUtil.initUnmarshaller;
import static it.petrovich.rss.xml.TestUtil.readXml;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlReaderTest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z");
    private static final String RSS_20_PATH = "/rss20-response.xml";
    private static final String ATOM_PATH = "/atom-response.xml";

    private static final JAXBContext ctx = initJaxbContext();
    private static final Unmarshaller unmarshaller = initUnmarshaller(ctx);

    @Test
    @SneakyThrows
    void testReadRss20() {
        final var rssObj = (TRss) unmarshaller.unmarshal(buildSource(readXml(RSS_20_PATH)), TRss.class).getValue();

        assertNotNull(rssObj);
        assertNotNull(rssObj.getVersion());
        assertNotNull(rssObj.getOtherAttributes());
        assertNotNull(rssObj.getAny());

        final var channel = rssObj.getChannel();
        assertNotNull(channel);
        assertNotNull(channel.getAny());
        assertNotNull(channel.getOtherAttributes());
        assertNotNull(channel.getTitleOrLinkOrDescription());

        final var items = channel.getItem();
        assertNotNull(items);
        assertEquals(12, items.size());

        assertAll(items.stream().map(item -> () -> {
            assertNotNull(item);
            assertNotNull(item.getOtherAttributes());
            assertNotNull(item.getTitleOrDescriptionOrLink());
        }));
    }

    @Test
    @SneakyThrows
    void testReadAtom() {
        final var feed = (FeedType) unmarshaller.unmarshal(buildSource(readXml(ATOM_PATH)), FeedType.class).getValue();

        assertNotNull(feed);
        assertNotNull(feed.getOtherAttributes());

        final var entries = feed.getAuthorOrCategoryOrContributor();
        assertNotNull(entries);
        assertEquals(26, entries.size());
    }

    @Test
    @SneakyThrows
    void testGetLastUpdateDate() {
        final var rssObj = (TRss) unmarshaller.unmarshal(buildSource(readXml(RSS_20_PATH)), TRss.class).getValue();

        final var dateString = rssObj.getChannel().getTitleOrLinkOrDescription().stream()
                .filter(item -> item.getClass().getSimpleName().equalsIgnoreCase(JAXBElement.class.getSimpleName()))
                .map(JAXBElement.class::cast)
                .filter(item -> item.getName().getLocalPart().equals("lastBuildDate"))
                .findFirst()
                .map(JAXBElement::getValue)
                .orElse(LocalDateTime.now().toString());
        assertNotNull(dateString);

        final var localDateTime = FORMATTER.parse((CharSequence) dateString);
        assertNotNull(localDateTime);
    }

    @Test
    @SneakyThrows
    void testUnmarshallAny() {
        final var feed = unmarshaller.unmarshal(buildSource(readXml(RSS_20_PATH)));
        assertNotNull(feed);
    }

    @Test
    @SneakyThrows
    void testReadJavaRevisited() {
        final var feed = unmarshaller.unmarshal(buildSource(readXml(ATOM_JAVAREVISITED_RESPONSE_XML)), FeedType.class).getValue();
        assertNotNull(feed);
    }
}