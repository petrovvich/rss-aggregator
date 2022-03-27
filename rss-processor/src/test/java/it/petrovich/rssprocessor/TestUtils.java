package it.petrovich.rssprocessor;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UtilityClass
public class TestUtils {

    public static Executable ann(Object actual) {
        return () -> assertNotNull(actual);
    }

    public static Executable ae(Object expected, Object actual) {
        return () -> assertEquals(expected, actual);
    }

    public static Executable at(Boolean actual) {
        return () -> assertTrue(actual);
    }
}
