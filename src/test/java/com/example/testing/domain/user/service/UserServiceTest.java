package com.example.testing.domain.user.service;

import com.example.testing.base.BaseServiceTest;
import com.example.testing.domain.user.dto.UserCreateRequestDto;
import com.example.testing.domain.user.dto.UserListResponseDto;
import com.example.testing.domain.user.entity.User;
import com.example.testing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest extends BaseServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getAllUsers() {

        List<User> users = List.of(
                new User("username4", "user4@test.com", "test1234"),
                new User("username5", "user5@test.com", "test1234"),
                new User("username6", "user6@test.com", "test1234")
        );

        // ID를 수동으로 설정하지 않기
        // saveAll 호출 시 ID를 null로 설정하여 JPA가 자동으로 ID를 생성하도록 합니다.
        userRepository.saveAll(users);

        var list = userService.getAllUsers();

        assertThat(list).isNotNull();
        assertThat(list).isInstanceOf(UserListResponseDto.class);
    }

    @Test
    void getUserById() {

        var newUser = new User("username3", "user3@test.com", "test1234");
        var savedUser = userRepository.save(newUser);

        var user = userService.getUserById(savedUser.getId());

        assertThat(user).isNotNull();
        assertThat(user.id()).isEqualTo(savedUser.getId());
        assertThat(user.name()).isEqualTo(savedUser.getName());
        assertThat(user.email()).isEqualTo(savedUser.getEmail());

    }

    @Test
    @Rollback(false)
    void createUser() {

        var userCreateRequestDto = new UserCreateRequestDto("username3", "user3@test.com", "test1234");

        var user = userService.createUser(userCreateRequestDto);

        assertThat(user).isNotNull();
        assertThat(user.id()).isNotNull();

        assertThat(user.email()).isEqualTo(userCreateRequestDto.email());
        assertThat(user.name()).isEqualTo(userCreateRequestDto.name());

    }

}