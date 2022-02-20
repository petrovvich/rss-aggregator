package it.petrovich.rssprocessor.service;

import it.petrovich.rss.validation.ArgumentValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ValidationService {
    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();
    private static final int EMPTY_SIZE = 0;

    public static void validate(final Object source) {
        val violations = VALIDATOR.validate(source);
        if (violations.size() > EMPTY_SIZE) {
            throw new ArgumentValidationException(source, extractMessage(violations));
        }
    }

    private static String extractMessage(final Set<ConstraintViolation<Object>> violations) {
        return violations
                .stream()
                .map(violation -> String.format("\t%s - %s", violation.getPropertyPath(), violation.getMessage()))
                .collect(Collectors.joining(";"));
    }
}
