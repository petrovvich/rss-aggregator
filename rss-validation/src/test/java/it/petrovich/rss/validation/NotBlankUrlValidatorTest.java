package it.petrovich.rss.validation;

import org.apache.commons.validator.routines.UrlValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NotBlankUrlValidatorTest {
    private static final String[] SCHEMES = {"http", "https"};
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(SCHEMES);

    @Test
    void testValidUrl() {
        assertTrue(URL_VALIDATOR.isValid("https://test.com/rss"));
    }

}