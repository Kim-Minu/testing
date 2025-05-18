package com.example.testing.domain.auth.dto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken,
        String tokenType
) {

} 