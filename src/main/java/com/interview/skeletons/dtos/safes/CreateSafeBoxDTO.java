package com.interview.skeletons.dtos.safes;

import com.interview.skeletons.validators.SecurePassword;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record CreateSafeBoxDTO(
    String name,
    @NotNull @SecurePassword String password
) implements Serializable {
}
