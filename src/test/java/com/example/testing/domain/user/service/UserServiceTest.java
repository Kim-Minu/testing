package com.example.testing.domain.user.service;

import com.example.testing.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void getAllUsers() {
        userService.getAllUsers();
    }

    @Test
    void getUserById() {

    }

    @Test
    void createUser() {

    }

}