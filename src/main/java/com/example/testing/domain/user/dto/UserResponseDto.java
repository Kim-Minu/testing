package com.example.testing.domain.user.dto;

import com.example.testing.domain.user.entity.User;

public record UserResponseDto(
        Long id,
        String name,
        String email
) {
    public UserResponseDto(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}
