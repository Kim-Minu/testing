package com.example.testing.domain.auth.service;


import com.example.testing.domain.auth.dto.LoginRequestDto;
import com.example.testing.domain.auth.dto.TokenResponseDto;

public interface AuthService {
    TokenResponseDto signIn(LoginRequestDto requestDto);
}
