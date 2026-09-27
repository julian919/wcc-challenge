package com.wcc.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePrincipalRequestDto(@NotBlank String username, @NotBlank String password) {
}
