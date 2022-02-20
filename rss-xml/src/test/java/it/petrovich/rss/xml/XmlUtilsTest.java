package it.petrovich.rss.xml;

import lombok.val;
import org.junit.jupiter.api.Test;

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
}