package com.assessment.personal_finance_tracker.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<IPasswordValidator, String> {

    @Override
    public void initialize(IPasswordValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*\\d.*") && // contains at least one digit
                password.matches(".*[a-z].*") && // contains at least one lowercase letter
                password.matches(".*[A-Z].*") && // contains at least one uppercase letter
                password.matches(".*[!@#$%^&*()].*"); // contains at least one special character
    }
}