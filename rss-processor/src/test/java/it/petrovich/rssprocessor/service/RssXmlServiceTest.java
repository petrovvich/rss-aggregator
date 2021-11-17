package it.petrovich.rssprocessor.service;

import it.petrovich.rss.xml.XmlConfiguration;
import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import it.petrovich.rssprocessor.dto.Feed;
import it.petrovich.rssprocessor.dto.Pair;
import it.petrovich.rssprocessor.dto.RssType;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RssXmlService.class, XmlConfiguration.class})
class RssXmlServiceTest {
    public static final String ATOM = "/atom-response.xml";
    public static final String RSS_20 = "/rss20-response.xml";
    @Autowired
    private RssXmlService service;

    @Test
    void testConvertAtom() {
        val convert = service.convert(buildPair(ATOM));

        assertNotNull(convert);
        assertEquals(FeedType.class.getSimpleName(), convert.rssEntry().getClass().getSimpleName());
    }

    @Test
    void testConvertRss20() {
        val convert = service.convert(buildPair(RSS_20));

        assertNotNull(convert);
        assertEquals(TRss.class.getSimpleName(), convert.rssEntry().getClass().getSimpleName());
    }

    private Pair<Feed, String> buildPair(String path) {
        return new Pair<>(buildFeed(null), readXml(path));
    }

    @SneakyThrows
    private Feed buildFeed(RssType type) {
        return new Feed(UUID.randomUUID(), "TEST_NAME", new URL("https://localhost.test/"), 1000, type);
    }

    @SneakyThrows
    private String readXml(String path) {
        return IOUtils.toString(
                Objects.requireNonNull(this.getClass().getResourceAsStream(path)),
                StandardCharsets.UTF_8);
    }
}