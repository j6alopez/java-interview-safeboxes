package com.interview.skeletons.validators;

import com.interview.skeletons.exceptions.SafeBoxException;
import com.interview.skeletons.exceptions.enums.SafeBoxErrorMessage;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurePasswordValidatorTest {

    private SecurePasswordValidator passwordValidator;
    private ConstraintValidatorContext context;
    private final int MINIMUM_LENGTH = 12;

    @BeforeEach
    void setUp() {
        passwordValidator = new SecurePasswordValidator();
        context = mockContext();
    }

    @Test
    void unsecured_password_length_ko() {
        final int length = RandomUtils.nextInt(0, MINIMUM_LENGTH);
        final String password = RandomStringUtils.random(length);

        final SafeBoxException exception = assertThrows(SafeBoxException.class, () -> {
            passwordValidator.isValid(password, context);
        });
        assertEquals(SafeBoxErrorMessage.INVALID_PASSWORD_FORMAT, exception.getErrorMessage());
    }

    @Test
    void unsecured_password_no_upper_case_ko() {
        final int length = RandomUtils.nextInt(MINIMUM_LENGTH, 20);
        final String password = RandomStringUtils.randomAlphanumeric(length).toLowerCase();

        final SafeBoxException exception = assertThrows(SafeBoxException.class, () -> {
            passwordValidator.isValid(password, context);
        });
        assertEquals(SafeBoxErrorMessage.INVALID_PASSWORD_FORMAT, exception.getErrorMessage());
    }

    @Test
    void unsecured_password_no_lower_case_ko() {
        final int length = RandomUtils.nextInt(MINIMUM_LENGTH, 20);
        final String password = RandomStringUtils.randomAlphanumeric(length).toUpperCase();

        final SafeBoxException exception = assertThrows(SafeBoxException.class, () -> {
            passwordValidator.isValid(password, context);
        });
        assertEquals(SafeBoxErrorMessage.INVALID_PASSWORD_FORMAT, exception.getErrorMessage());
    }

    @Test
    void unsecured_password_no_numbers_case_ko() {
        final int length = RandomUtils.nextInt(MINIMUM_LENGTH, 20);
        final String password = RandomStringUtils.randomAlphabetic(length).toUpperCase();
        final SafeBoxException exception = assertThrows(SafeBoxException.class, () -> {
            passwordValidator.isValid(password, context);
        });
        assertEquals(SafeBoxErrorMessage.INVALID_PASSWORD_FORMAT, exception.getErrorMessage());
    }

    @Test
    void unsecured_password_no_special_character_case_ko() {
        final String testPassword = "Abcdee12jsdfuewkurjdkchskj";
        final SafeBoxException exception = assertThrows(SafeBoxException.class, () -> {
            passwordValidator.isValid(testPassword, context);
        });
        assertEquals(SafeBoxErrorMessage.INVALID_PASSWORD_FORMAT, exception.getErrorMessage());
    }

    private ConstraintValidatorContext mockContext() {
        final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        final ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder = mock(
            ConstraintValidatorContext.ConstraintViolationBuilder.class
        );
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        return context;
    }

}
