package com.example.testing.domain.user.dto;

import com.example.testing.domain.user.entity.Role;
import com.example.testing.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;

public record UserCreateRequestDto(
        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다.")
        String name,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "유효한 이메일 주소여야 합니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password

) {
    public User toEntity() {

        var roles = new HashSet<Role>();
        roles.add(Role.USER);

        return User.builder()
                .name(name)
                .email(email)
                .password(new BCryptPasswordEncoder().encode(password))
                .roles(roles)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();
    }
}
