package com.example.testing.domain.user.service;

import com.example.testing.domain.user.dto.UserCreateRequestDto;
import com.example.testing.domain.user.entity.User;
import com.example.testing.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getAllUsers() {
        List<User> users = List.of(
                new User(null, "user1", "user1@test.com"),
                new User(null, "user2", "user2@test.com"),
                new User(null, "user3", "user3@test.com")
        );

        // ID를 수동으로 설정하지 않기
        // saveAll 호출 시 ID를 null로 설정하여 JPA가 자동으로 ID를 생성하도록 합니다.
        userRepository.saveAll(users);

        var list = userService.getAllUsers();

        assertThat(list).isNotNull();
        assertThat(list).hasSize(3);
    }

    @Test
    void getUserById() {

        var newUser = new User(null, "user1", "user1@test.com");
        var savedUser = userRepository.save(newUser);

        var user = userService.getUserById(savedUser.getId());

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(savedUser.getId());
        assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());

    }

    @Test
    void createUser() {

        var userCreateRequestDto = new UserCreateRequestDto("user1", "user1@test.com");

        var user = userService.createUser(userCreateRequestDto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();

        assertThat(user.getEmail()).isEqualTo(userCreateRequestDto.email());
        assertThat(user.getUsername()).isEqualTo(userCreateRequestDto.username());

    }

}