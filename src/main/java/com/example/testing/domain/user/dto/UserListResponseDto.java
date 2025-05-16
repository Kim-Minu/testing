package com.example.testing.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserListResponseDto {
    List<UserResponseDto> userList;
}
