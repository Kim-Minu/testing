package com.example.testing.domain.user.dto;

import java.util.List;

public record UserListResponseDto(
    List<UserResponseDto> userList
) {

}
