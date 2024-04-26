package com.example.javapracticaltestassignment.lib;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinAgeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinAge {
    String message() default "Invalid birthdate";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
