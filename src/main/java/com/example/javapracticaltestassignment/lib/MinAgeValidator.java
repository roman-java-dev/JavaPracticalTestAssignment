package com.example.javapracticaltestassignment.lib;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate> {
    @Value("${api.user.min-age}")
    private int minAge;
    @Value("${api.user.min-age.message}")
    private String message;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate minDate = currentDate.minusYears(minAge);
        if (value.isAfter(minDate)) {
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }
}
