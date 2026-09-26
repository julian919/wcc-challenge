package com.wcc.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientTokenRequestDto(@NotBlank String clientId, @NotBlank String clientSecret) {
}
