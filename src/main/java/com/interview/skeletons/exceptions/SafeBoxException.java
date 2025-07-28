package com.interview.skeletons.exceptions;

import com.interview.skeletons.exceptions.enums.SafeBoxErrorMessage;
import lombok.Getter;

@Getter
public class SafeBoxException extends RuntimeException {
    private SafeBoxErrorMessage errorMessage;

    public SafeBoxException(SafeBoxErrorMessage errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }
}
