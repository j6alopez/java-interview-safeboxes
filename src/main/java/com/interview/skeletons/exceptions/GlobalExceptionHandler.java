package com.interview.skeletons.exceptions;

import com.interview.skeletons.dtos.exceptions.ApiSafeBoxError;
import com.interview.skeletons.exceptions.enums.SafeBoxErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SafeBoxException.class)
    public ResponseEntity<Object> handleSafeBoxException(SafeBoxException exception) {
        SafeBoxErrorMessage errorMessage = exception.getErrorMessage();
        HttpStatus code = SafeBoxErrorMessage.getHttpStatus(errorMessage);
        ApiSafeBoxError apiSafeBoxError = convert(errorMessage);
        return new ResponseEntity<>(apiSafeBoxError, code);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException() {
        SafeBoxErrorMessage errorMessage = SafeBoxErrorMessage.INTERNAL_SERVER_ERROR;
        HttpStatus code = SafeBoxErrorMessage.getHttpStatus(errorMessage);
        ApiSafeBoxError apiSafeBoxError = convert(errorMessage);
        return new ResponseEntity<>(apiSafeBoxError, code);
    }

    private ApiSafeBoxError convert(SafeBoxErrorMessage errorMessage) {
        String message = SafeBoxErrorMessage.getMessage(errorMessage);
        return new ApiSafeBoxError(errorMessage, message);
    }

}
