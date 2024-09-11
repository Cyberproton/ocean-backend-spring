package me.cyberproton.ocean.features.history.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsValidHistoryValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidHistory {
    String message() default "Invalid history";

    Class[] groups() default {};

    Class[] payload() default {};
}
