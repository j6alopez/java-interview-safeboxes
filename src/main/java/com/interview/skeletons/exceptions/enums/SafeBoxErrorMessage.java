package com.interview.skeletons.exceptions.enums;

import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum SafeBoxErrorMessage {
    SAFE_BOX_NOT_FOUND(HttpStatus.NOT_FOUND, "SafeBox Id was not found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong happened"),
    INVALID_PASSWORD_FORMAT(HttpStatus.UNPROCESSABLE_ENTITY, "Password is not enough secure");

    private final HttpStatus httpStatus;
    private final String description;

    private HttpStatus getHttpStatus() {
        return httpStatus;
    }

    private String getDescription() {
        return description;
    }

    private SafeBoxErrorMessage(HttpStatus httpStatus, String description) {
        this.httpStatus = httpStatus;
        this.description = description;
    }

    public static String getMessage(SafeBoxErrorMessage errorMessage) {
        return Arrays.stream(SafeBoxErrorMessage.values())
            .filter(element -> errorMessage.equals(element))
            .findFirst()
            .map(element -> element.getDescription())
            .orElse(null);
    }

    public static SafeBoxErrorMessage getFromHttpStatus(HttpStatus code) {
        return Arrays.stream(SafeBoxErrorMessage.values())
            .filter(element -> element.getHttpStatus().equals(code))
            .findFirst()
            .orElse(INTERNAL_SERVER_ERROR);
    }

    public static HttpStatus getHttpStatus(SafeBoxErrorMessage errorMessage) {
        return Arrays.stream(SafeBoxErrorMessage.values())
            .filter(element -> errorMessage.equals(element))
            .findFirst()
            .map(element -> element.getHttpStatus())
            .orElse(null);
    }

}
