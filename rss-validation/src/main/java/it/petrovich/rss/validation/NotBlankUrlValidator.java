package it.petrovich.rss.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * Validator for {@link NotBlankUrl} annotation.
 * Validation based on the UrlValidator from the apache validation library.
 */
public class NotBlankUrlValidator implements ConstraintValidator<NotBlankUrl, Object> {

    /**
     * Schemes supports by the validator.
     * Only http and the https, because at the moment rss can not be load by the another scheme.
     */
    private static final String[] SCHEMES = {"http", "https"};
    /**
     * @see UrlValidator
     */
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(SCHEMES);

    /**
     * @see ConstraintValidator
     */
    @Override
    public void initialize(final NotBlankUrl constraintAnnotation) {

    }

    /**
     * @see ConstraintValidator
     */
    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        if (value instanceof String url) {
            return URL_VALIDATOR.isValid(url);
        }
        return true;
    }
}
