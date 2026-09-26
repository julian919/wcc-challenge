package com.wcc.auth.dto;

public record TokenResponseDto(String accessToken, String tokenType, long expiresIn) {
}
