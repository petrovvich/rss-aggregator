package it.petrovich.rss.xml;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

import static it.petrovich.rss.xml.XmlUtils.extractParagraphs;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlUtilsTest {

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

    private String readFile(String path) throws IOException {
        return new String(Objects.requireNonNull(this.getClass().getResourceAsStream(path)).readAllBytes());
    }
}