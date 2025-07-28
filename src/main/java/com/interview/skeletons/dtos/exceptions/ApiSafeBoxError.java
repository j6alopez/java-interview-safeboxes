package com.interview.skeletons.dtos.exceptions;

import com.interview.skeletons.exceptions.enums.SafeBoxErrorMessage;

import java.io.Serializable;

public record ApiSafeBoxError(
    SafeBoxErrorMessage code,
    String message
) implements Serializable {
}
