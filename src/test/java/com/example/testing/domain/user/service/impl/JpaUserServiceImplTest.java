package com.example.testing.domain.user.service.impl;

import com.example.testing.domain.user.entity.User;
import com.example.testing.domain.user.repository.UserRepository;
import com.example.testing.domain.user.service.UserService;
import com.example.testing.global.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

        when(userRepository.findAll()).thenReturn(users);

        assertThat(userService.getAllUsers()).hasSize(3);

        verify(userRepository, times(1)).findAll();

    }

    @Test
    void getUserById() {
        var user = new User(1L, "user1", "test1@test.com");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        var result = userService.getUserById(1L);

        assertThat(result).isEqualTo(user);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createUser_ValidUser() {
        var user = new User(null, "user1", "test1@test.com");
        var savedUser = new User(1L, "user1", "test1@test.com");

        when(userRepository.save(user)).thenReturn(savedUser);

        var result = userService.createUser(user);

        assertThat(result).isEqualTo(savedUser);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_MissingUsername() {
        var user = new User(null, null, "test1@test.com");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_InvalidEmail() {
        var user = new User(null, "user1", "invalid-email");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));

        verify(userRepository, never()).save(any());
    }

}