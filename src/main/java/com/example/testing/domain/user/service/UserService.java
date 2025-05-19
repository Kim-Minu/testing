package com.example.testing.domain.user.service;


import com.example.testing.domain.user.dto.UserCreateRequestDto;
import com.example.testing.domain.user.dto.UserListResponseDto;
import com.example.testing.domain.user.dto.UserResponseDto;

public interface UserService {
    UserListResponseDto getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto createUser(UserCreateRequestDto requestDto);
}