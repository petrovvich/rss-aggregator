package it.petrovich.rss.domain;

import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static it.petrovich.rss.domain.XmlUtils.atomContentOrElse;
import static it.petrovich.rss.domain.XmlUtils.atomLinkOrElse;
import static it.petrovich.rss.domain.XmlUtils.extractEntries;
import static it.petrovich.rss.domain.XmlUtils.extractParagraphs;
import static it.petrovich.rss.domain.XmlUtils.extractTextOrElse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlUtilsTest {

    private static final JAXBContext ctx = TestUtil.initJaxbContext();
    private static final Unmarshaller unmarshaller = TestUtil.initUnmarshaller(ctx);

    @Test
    void testCast_shouldReturnExpectedString() {
        Object source = "test";
        final var actual = XmlUtils.castToString(source);
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("test", actual)
        );
    }

    @Test
    void testCast_shouldReturnEmptyString() {
        Object source = 12;
        final var actual = XmlUtils.castToString(source);
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("", actual)
        );
    }

    @Test
    void testHtmlToTextNonHtml_shouldReturnExpectedString() {
        final var actual = extractParagraphs("Hello world!");

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("Hello world!", actual)
        );
    }

    @Test
    @SneakyThrows
    void testHtmlToText_shouldReturnValidTextFromPTags() {
        final var source = readFile("/testHtmlToText_shouldReturnValidTextFromPTags_source.txt");
        final var expected = readFile("/testHtmlToText_shouldReturnValidTextFromPTags_expected.txt");
        final var actual = extractParagraphs(source);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected, actual)
        );
    }

    @Test
    @SneakyThrows
    void testExtractEntryAtom_shouldReturnExpectedElements() {
        final var feed = unmarshaller.unmarshal(TestUtil.buildSource(TestUtil.readXml(TestUtil.ATOM_JAVAREVISITED_RESPONSE_XML)),
                FeedType.class).getValue();

        final var actual = extractEntries(feed);
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(11, actual.size())
        );

    }

    @Test
    @SneakyThrows
    void testExtractTextOrElse_shouldReturnExpectedElementValue() {
        final var feed = unmarshaller.unmarshal(TestUtil.buildSource(TestUtil.readXml(TestUtil.ATOM_JAVAREVISITED_RESPONSE_XML)),
                FeedType.class).getValue();

        final var entryType = extractEntries(feed).stream().findFirst().get();
        final var actual = extractTextOrElse(entryType, "title", "");
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("How to log SQL statements in Spring Boot? Example Tutorial", actual)
        );
    }

    @Test
    @SneakyThrows
    void testExtractLinkOrElse_shouldReturnExpectedElementValue() {
        final var feed = unmarshaller.unmarshal(TestUtil.buildSource(TestUtil.readXml(TestUtil.ATOM_JAVAREVISITED_RESPONSE_XML)),
                FeedType.class).getValue();

        final var entryType = extractEntries(feed).stream().findFirst().get();
        final var actual = atomLinkOrElse(entryType, "link", "");
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("http://javarevisited.blogspot.com/2022/02/how-to-log-sql-statements-in-spring.html",
                        actual)
        );
    }

    @Test
    @SneakyThrows
    void testExtractLinkOrElse_shouldReturnExpectedElement() {
        final var feed = unmarshaller.unmarshal(TestUtil.buildSource(TestUtil.readXml("/atom-java67-response.xml")),
                FeedType.class).getValue();

        final var entryType = extractEntries(feed).stream().findFirst().get();
        final var actual = atomLinkOrElse(entryType, "link", "");
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("http://www.java67.com/2022/02/how-to-find-longest-common-prefix-in.html",
                        actual)
        );
    }

    @Test
    @SneakyThrows
    void testExtractDescriptionOrElse_shouldReturnExpectedElement() {
        final var feed = unmarshaller.unmarshal(TestUtil.buildSource(TestUtil.readXml("/atom-spring.xml")),
                FeedType.class).getValue();

        final var entryType = extractEntries(feed).stream().findFirst().get();
        final var actual = atomContentOrElse(entryType, "content", "");
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(readFile("/testExtractDescriptionOrElse_expected.txt"), actual)
        );
    }

    @Test
    void testExtractParagraphsAtom() {
        final var actual = extractParagraphs(readFile("/testExtractDescriptionOrElse_expected.txt"));
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(readFile("/testExtractParagraphsAtom_expected.txt"), actual)
        );
    }

    @Test
    @SneakyThrows
    void testAtom_ShouldNotThrowException() {
        final var feed = unmarshaller.unmarshal(TestUtil.buildSource(TestUtil.readXml(TestUtil.ATOM_RESPONSE_NEW)),
                TRss.class).getValue();

        assertAll(
                () -> assertNotNull(feed)
        );
    }

    @SneakyThrows
    private String readFile(String path) {
        return new String(Objects.requireNonNull(this.getClass().getResourceAsStream(path)).readAllBytes());
    }
}