package it.petrovich.rssprocessor.validation;

import it.petrovich.rssprocessor.dto.FeedSettingsRequest;
import org.apache.commons.validator.routines.UrlValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for {@link NotBlankUrl} annotation.
 */
public class NotBlankUrlValidator implements ConstraintValidator<NotBlankUrl, Object> {

    private static final String[] SCHEMES = {"http", "https"};
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(SCHEMES);

    @Override
    public void initialize(NotBlankUrl constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof String url) {
            return URL_VALIDATOR.isValid(url);
        }

        return true;
    }
}