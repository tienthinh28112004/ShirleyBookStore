package ApiWebManga.validator.anotation;

import ApiWebManga.validator.DobValidator;
import ApiWebManga.validator.MinSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MinSizeAnotation implements ConstraintValidator<MinSizeValidator, String> {
    private int min;
    @Override
    public void initialize(MinSizeValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min=constraintAnnotation.min();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.length()>=min;
    }
}

