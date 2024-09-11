package me.cyberproton.ocean.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsEmailOrUsernameValidator implements ConstraintValidator<IsEmailOrUsername, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
                || s.matches("^[a-zA-Z0-9._%+-]{6,}$");
    }
}
