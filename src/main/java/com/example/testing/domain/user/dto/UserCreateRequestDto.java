package com.example.testing.domain.user.dto;

import com.example.testing.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDto(
        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다.")
        String username,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "유효한 이메일 주소여야 합니다.")
        String email

) {
    public User toEntity() {
        return User.builder()
                .username(username)
                .email(email)
                .build();
    }
}
