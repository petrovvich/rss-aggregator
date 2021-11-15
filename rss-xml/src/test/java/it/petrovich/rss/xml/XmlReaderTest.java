package it.petrovich.rss.xml;

import jakarta.xml.bind.JAXBContext;

import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlReaderTest {
    private static final String RSS_20_PATH = "/rss20-response.xml";
    private static final String ATOM_PATH = "/atom-response.xml";

    @Test
    @SneakyThrows
    void testGrabRss20() {
        val context = JAXBContext.newInstance(TRss.class);
        val unmarshaller = context.createUnmarshaller();
        val source = new StreamSource(readXml(RSS_20_PATH));
        val rssObj = (TRss) unmarshaller.unmarshal(source, TRss.class).getValue();

        assertNotNull(rssObj);
        assertNotNull(rssObj.getVersion());
        assertNotNull(rssObj.getOtherAttributes());
        assertNotNull(rssObj.getAny());

        val channel = rssObj.getChannel();
        assertNotNull(channel);
        assertNotNull(channel.getAny());
        assertNotNull(channel.getOtherAttributes());
        assertNotNull(channel.getTitleOrLinkOrDescription());

        val items = channel.getItem();
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
    void testGrabRssAtom() {
        val context = JAXBContext.newInstance(FeedType.class);
        var unmarshaller = context.createUnmarshaller();
        var source = new StreamSource(readXml(ATOM_PATH));
        var rssObj = (FeedType) unmarshaller.unmarshal(source, FeedType.class).getValue();

        assertNotNull(rssObj);
        assertNotNull(rssObj.getOtherAttributes());
        assertNotNull(rssObj.getAuthorOrCategoryOrContributor());
    }

    @SneakyThrows
    private InputStream readXml(String path) {
        return Objects.requireNonNull(this.getClass().getResourceAsStream(path));
    }
}