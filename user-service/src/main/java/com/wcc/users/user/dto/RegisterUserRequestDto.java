package com.wcc.users.user.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequestDto(@NotBlank String username, @NotBlank String password,
                                     @NotBlank String firstName, @NotBlank String lastName) {
}
