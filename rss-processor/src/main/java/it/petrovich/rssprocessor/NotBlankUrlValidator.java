package it.petrovich.rssprocessor;

import org.apache.commons.validator.routines.UrlValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
        if (value instanceof FeedSettings settings) {
            return URL_VALIDATOR.isValid(settings.url());
        }

        return true;
    }
}