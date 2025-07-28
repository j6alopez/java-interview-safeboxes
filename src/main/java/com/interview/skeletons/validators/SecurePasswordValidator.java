package com.interview.skeletons.validators;

import com.interview.skeletons.exceptions.SafeBoxException;
import com.interview.skeletons.exceptions.enums.SafeBoxErrorMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SecurePasswordValidator implements ConstraintValidator<SecurePassword, String> {

    @Override
    public void initialize(SecurePassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (!isSecurePassword(password)) {
            SafeBoxErrorMessage error = SafeBoxErrorMessage.INVALID_PASSWORD_FORMAT;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(SafeBoxErrorMessage.getMessage(error));
            throw new SafeBoxException(error);
        }
        return true;

    }

    private boolean isSecurePassword(String password) {
        final int MINIMUM_PASSWORD_LENGTH = 12;
        if (password.length() < MINIMUM_PASSWORD_LENGTH) {
            return false;
        }
        final String UPPER_CASE_REG_EX = ".*[A-Z].*";
        final String LOWER_CASE_REG_EX = ".*[a-z].*";
        final String DIGIT_REG_EX = ".*\\d.*";
        final String SPECIAL_CHAR_REG_EX = ".*[!@#$%^&*()].*";

        return Pattern.matches(UPPER_CASE_REG_EX, password) &&
            Pattern.matches(LOWER_CASE_REG_EX, password) &&
            Pattern.matches(DIGIT_REG_EX, password) &&
            Pattern.matches(SPECIAL_CHAR_REG_EX, password);

    }
}
