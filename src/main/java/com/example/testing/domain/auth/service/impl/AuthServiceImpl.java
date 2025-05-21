package com.example.testing.domain.auth.service.impl;

import com.example.testing.domain.auth.dto.LoginRequestDto;
import com.example.testing.domain.auth.dto.TokenResponseDto;
import com.example.testing.domain.auth.service.AuthService;
import com.example.testing.domain.user.exception.UserSignInException;
import com.example.testing.global.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponseDto signIn(LoginRequestDto requestDto) {

        try {
            //아이디 비밀번호 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.email(),
                            requestDto.password()
                    )
            );

            if(!authentication.isAuthenticated()){
                throw new UserSignInException("아이디 또는 비밀번호를 확인해주세요.");
            }

            String name = authentication.getName();
            String accessToken = jwtTokenProvider.createAccessToken(name);
            String refreshToken = jwtTokenProvider.createRefreshToken(name);

            return new TokenResponseDto(accessToken, refreshToken, "Bearer");

        } catch (DisabledException | LockedException | BadCredentialsException e) {
            throw new UserSignInException(e.getMessage());
        }

    }
}
