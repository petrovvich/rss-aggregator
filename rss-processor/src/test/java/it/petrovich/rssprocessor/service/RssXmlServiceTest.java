package it.petrovich.rssprocessor.service;

import it.petrovich.rss.common.Feed;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.RssType;
import it.petrovich.rss.xml.XmlConfiguration;
import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import it.petrovich.rssprocessor.converter.AtomConverter;
import it.petrovich.rssprocessor.converter.Rss20Converter;
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

import static it.petrovich.rssprocessor.TestUtils.ae;
import static it.petrovich.rssprocessor.TestUtils.ann;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RssXmlService.class, XmlConfiguration.class, AtomConverter.class,
        Rss20Converter.class})
class RssXmlServiceTest {
    public static final String ATOM = "/atom-response.xml";
    public static final String RSS_20 = "/rss20-response.xml";
    @Autowired
    private RssXmlService service;

    @Test
    void testConvert_shouldReturnAtom() {
        val actual = service.convert(buildPair(ATOM));

        assertAll(
                ann(actual),
                ae(FeedType.class.getSimpleName(), actual.rssEntry().getClass().getSimpleName())
        );
    }

    @Test
    void testConvertRss20() {
        val actual = service.convert(buildPair(RSS_20));

        assertAll(
                ann(actual),
                ae(TRss.class.getSimpleName(), actual.rssEntry().getClass().getSimpleName())
        );
    }

    @Test
    void testResolve_shouldReturnAtom() {
        // when
        val actual = service.resolve(readXml(ATOM));

        // then
        assertEquals(RssType.ATOM, actual);
    }

    @Test
    void testResolve_shouldReturnRss20() {
        // when
        val actual = service.resolve(readXml(RSS_20));

        // then
        assertEquals(RssType.RSS20, actual);
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