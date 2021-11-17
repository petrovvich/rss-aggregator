package it.petrovich.rss.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * Validator for {@link NotBlankUrl} annotation.
 * Validation based on the UrlValidator from the apache validation library.
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
