package com.example.testing.domain.user.service.impl;

import com.example.testing.base.BaseServiceImplTest;
import com.example.testing.domain.user.dto.UserCreateRequestDto;
import com.example.testing.domain.user.dto.UserListResponseDto;
import com.example.testing.domain.user.dto.UserResponseDto;
import com.example.testing.domain.user.entity.User;
import com.example.testing.domain.user.exception.EmailDuplicateException;
import com.example.testing.domain.user.repository.UserRepository;
import com.example.testing.global.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class JpaUserServiceImplTest extends BaseServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void getAllUsers() {
        var users = List.of(
                new User(1L, "user1", "test1@test.com"),
                new User(2L, "user2", "test2@test.com"),
                new User(3L, "user3", "test@3test.com")
        );

        given(userRepository.findAll()).willReturn(users);

        assertThat(userService.getAllUsers()).isInstanceOf(UserListResponseDto.class);

        then(userRepository).should(times(1)).findAll();

    }

    @Test
    void getUserById() {
        var user = new User(1L, "user1", "test1@test.com");

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));

        var result = userService.getUserById(1L);

        assertThat(result).isInstanceOf(UserResponseDto.class);

        then(userRepository).should(times(1)).findById(1L);

    }

    @Test
    void getUserById_NotFound() {
        given(userRepository.findById(1L)).willReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));

        then(userRepository).should(times(1)).findById(1L);

    }

    @Test
    void createUser_ValidUser() {

        var userCreateRequestDto = new UserCreateRequestDto("user1", "test1@test.com");

        var savedUser = new User(1L, "user1", "test1@test.com");

        given(userRepository.save(any())).willReturn(savedUser);

        var result = userService.createUser(userCreateRequestDto);

        assertThat(result).isInstanceOf(UserResponseDto.class);

        then(userRepository).should(times(1)).save(any());

    }

    @Test
    void create_Email_Duplicate() {

        var userCreateRequestDto = new UserCreateRequestDto("user1", "test1@test.com");

        given(userRepository.existsByEmail(any())).willReturn(true);

        assertThrows(EmailDuplicateException.class, () -> userService.createUser(userCreateRequestDto));

        then(userRepository).should(times(1)).existsByEmail(any());

    }


}