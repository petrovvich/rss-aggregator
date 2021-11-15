package it.petrovich.rssprocessor.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated element must be not blank and valid URL.
 */
@Documented
@Constraint(validatedBy = NotBlankUrlValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankUrl {

    String message() default "{subscription.url.not.valid.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
