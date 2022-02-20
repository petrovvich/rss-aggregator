package it.petrovich.rss.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element must be not blank and valid URL.
 * For validation purposes uses the UrlValidator from the apache validator library.
 */
@Documented
@Constraint(validatedBy = NotBlankUrlValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankUrl {

    /**
     * Message that shows in the exception when constraint check fails.
     * Configuration is stored in the ValidationMessages.properties resource file.
     *
     * @return constraint violation message
     */
    String message() default "{subscription.url.not.valid.message}";

    /**
     * An attribute groups that allows the specification of validation groups, to which this constraint belongs.
     * This must default to an empty array of type Class<?>.
     *
     * @return attribute groups
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Bean Validation API to assign custom payload objects to a constraint.
     * This attribute is not used by the API itself.
     *
     * @return custom payload objects
     */
    Class<? extends Payload>[] payload() default {};
}
