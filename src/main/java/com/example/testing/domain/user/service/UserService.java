package com.example.testing.domain.user.service;


import com.example.testing.domain.user.dto.UserCreateRequestDto;
import com.example.testing.domain.user.dto.UserListResponseDto;
import com.example.testing.domain.user.dto.UserResponseDto;
import com.example.testing.domain.user.entity.User;

import java.util.List;

public interface UserService {
    UserListResponseDto getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto createUser(UserCreateRequestDto requestDto);
}