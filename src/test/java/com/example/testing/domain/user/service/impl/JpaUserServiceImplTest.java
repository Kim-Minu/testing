package com.example.testing.domain.user.service.impl;

import com.example.testing.domain.user.dto.UserCreateRequestDto;
import com.example.testing.domain.user.entity.User;
import com.example.testing.domain.user.repository.UserRepository;
import com.example.testing.global.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaUserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    JpaUserServiceImpl userService;

    @Test
    void getAllUsers() {
        var users = List.of(
                new User(1L, "user1", "test1@test.com"),
                new User(2L, "user2", "test2@test.com"),
                new User(3L, "user3", "test@3test.com")
        );

        given(userRepository.findAll()).willReturn(users);

        assertThat(userService.getAllUsers()).hasSize(3);

        then(userRepository).should(times(1)).findAll();

    }

    @Test
    void getUserById() {
        var user = new User(1L, "user1", "test1@test.com");

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));

        var result = userService.getUserById(1L);

        assertThat(result).isEqualTo(user);

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

        assertThat(result).isEqualTo(savedUser);

        then(userRepository).should(times(1)).save(any());

    }

    @Test
    void createUser_MissingUsername() {
        var userCreateRequestDto = new UserCreateRequestDto("user1", "test1@test.com");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userCreateRequestDto));

        then(userRepository).should(never()).save(any());
    }

    @Test
    void createUser_InvalidEmail() {
        var userCreateRequestDto = new UserCreateRequestDto("user1", "test1@test.com");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userCreateRequestDto));

        then(userRepository).should(never()).save(any());
    }



}