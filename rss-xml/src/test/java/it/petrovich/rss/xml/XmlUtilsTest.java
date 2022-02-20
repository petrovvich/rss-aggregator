package it.petrovich.rss.xml;

import it.petrovich.rss.xml.atom.FeedType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static it.petrovich.rss.xml.TestUtil.ATOM_JAVAREVISITED_RESPONSE_XML;
import static it.petrovich.rss.xml.TestUtil.buildSource;
import static it.petrovich.rss.xml.TestUtil.initJaxbContext;
import static it.petrovich.rss.xml.TestUtil.initUnmarshaller;
import static it.petrovich.rss.xml.TestUtil.readXml;
import static it.petrovich.rss.xml.XmlUtils.atomContentOrElse;
import static it.petrovich.rss.xml.XmlUtils.atomLinkOrElse;
import static it.petrovich.rss.xml.XmlUtils.extractEntries;
import static it.petrovich.rss.xml.XmlUtils.extractParagraphs;
import static it.petrovich.rss.xml.XmlUtils.extractTextOrElse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlUtilsTest {

    private static final JAXBContext ctx = initJaxbContext();
    private static final Unmarshaller unmarshaller = initUnmarshaller(ctx);

    @Test
    void testCast_shouldReturnExpectedString() {
        Object source = "test";
        val actual = XmlUtils.castToString(source);
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("test", actual)
        );
    }

    @Test
    void testCast_shouldReturnEmptyString() {
        Object source = 12;
        val actual = XmlUtils.castToString(source);
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("", actual)
        );
    }

    @Test
    void testHtmlToTextNonHtml_shouldReturnExpectedString() {
        val actual = extractParagraphs("Hello world!");

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("Hello world!", actual)
        );
    }

    @Test
    @SneakyThrows
    void testHtmlToText_shouldReturnValidTextFromPTags() {
        val source = readFile("/testHtmlToText_shouldReturnValidTextFromPTags_source.txt");
        val expected = readFile("/testHtmlToText_shouldReturnValidTextFromPTags_expected.txt");
        val actual = extractParagraphs(source);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected, actual)
        );
    }

    @Test
    @SneakyThrows
    void testExtractEntryAtom_shouldReturnExpectedElements() {
        val feed = unmarshaller.unmarshal(buildSource(readXml(ATOM_JAVAREVISITED_RESPONSE_XML)),
                FeedType.class).getValue();

        val actual = extractEntries(feed);
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(11, actual.size())
        );

    }

    @Test
    @SneakyThrows
    void testExtractTextOrElse_shouldReturnExpectedElementValue() {
        val feed = unmarshaller.unmarshal(buildSource(readXml(ATOM_JAVAREVISITED_RESPONSE_XML)),
                FeedType.class).getValue();

        val entryType = extractEntries(feed).stream().findFirst().get();
        val actual = extractTextOrElse(entryType, "title", "");
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("How to log SQL statements in Spring Boot? Example Tutorial", actual)
        );
    }

    @Test
    @SneakyThrows
    void testExtractLinkOrElse_shouldReturnExpectedElementValue() {
        val feed = unmarshaller.unmarshal(buildSource(readXml(ATOM_JAVAREVISITED_RESPONSE_XML)),
                FeedType.class).getValue();

        val entryType = extractEntries(feed).stream().findFirst().get();
        val actual = atomLinkOrElse(entryType, "link", "");
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("http://javarevisited.blogspot.com/2022/02/how-to-log-sql-statements-in-spring.html",
                        actual)
        );
    }

    @Test
    @SneakyThrows
    void testExtractLinkOrElse_shouldReturnExpectedElement() {
        val feed = unmarshaller.unmarshal(buildSource(readXml("/atom-java67-response.xml")),
                FeedType.class).getValue();

        val entryType = extractEntries(feed).stream().findFirst().get();
        val actual = atomLinkOrElse(entryType, "link", "");
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("http://www.java67.com/2022/02/how-to-find-longest-common-prefix-in.html",
                        actual)
        );
    }

    @Test
    @SneakyThrows
    void testExtractDescriptionOrElse_shouldReturnExpectedElement() {
        val feed = unmarshaller.unmarshal(buildSource(readXml("/atom-spring.xml")),
                FeedType.class).getValue();

        val entryType = extractEntries(feed).stream().findFirst().get();
        val actual = atomContentOrElse(entryType, "content", "");
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(readFile("/testExtractDescriptionOrElse_expected.txt"), actual)
        );
    }

    @Test
    void testExtractParagraphsAtom() {
        val actual = extractParagraphs(readFile("/testExtractDescriptionOrElse_expected.txt"));
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(readFile("/testExtractParagraphsAtom_expected.txt"), actual)
        );
    }

    @SneakyThrows
    private String readFile(String path) {
        return new String(Objects.requireNonNull(this.getClass().getResourceAsStream(path)).readAllBytes());
    }
}