package com.example.testing.domain.user.controller;

import com.example.testing.base.BaseControllerTest;
import com.example.testing.domain.user.dto.UserCreateRequestDto;
import com.example.testing.domain.user.entity.User;
import com.example.testing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.example.testing.global.exception.ErrorCode.*;

class UserControllerTest extends BaseControllerTest {

    @Autowired
    UserRepository userRepository;

    List<User> users;

    private final String BASE_URL = "/api/v1/users";

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        users = List.of(
                new User("username4", "user4@test.com", "test1234"),
                new User("username5", "user5@test.com", "test1234"),
                new User("username6", "user6@test.com", "test1234")
        );

        // ID를 수동으로 설정하지 않기
        // saveAll 호출 시 ID를 null로 설정하여 JPA가 자동으로 ID를 생성하도록 합니다.
        userRepository.saveAll(users);
    }


    @Test
    @DisplayName("모든 사용자 조회")
    void getAllUsers() throws Exception {

        var result = apiCallForGetMethod(BASE_URL, (Object) null);

        var resultBodyJson = result.bodyJson();

        result.hasStatus(HttpStatus.OK);

       /* resultBodyJson.extractingPath("$.userList[0].name").isEqualTo(users.get(0).getName());
        resultBodyJson.extractingPath("$.userList[0].email").isEqualTo(users.get(0).getEmail());

        resultBodyJson.extractingPath("$.userList[1].name").isEqualTo(users.get(1).getName());
        resultBodyJson.extractingPath("$.userList[1].email").isEqualTo(users.get(1).getEmail());*/

    }


    @Nested
    @DisplayName("사용자 조회 관련 테스트")
    class RetrievalTests {

        @Test
        @DisplayName("사용자 ID로 조회")
        void getUserById() throws Exception {

            Long userId = users.getFirst().getId();

            var result = apiCallForGetMethod(BASE_URL+"/{userId}", userId);
            var resultBodyJson = result.bodyJson();

            result.hasStatus(HttpStatus.OK);

            resultBodyJson.extractingPath("$.name").isEqualTo(users.getFirst().getName());
            resultBodyJson.extractingPath("$.email").isEqualTo(users.getFirst().getEmail());
        }

        @Test
        @DisplayName("사용자 ID로 조회 - 사용자 없음")
        void getUserById_NotFound() throws Exception {

            Long nonExistentId = 0L;

            var result = apiCallForGetMethod(BASE_URL+"/{userId}", nonExistentId);
            var resultBodyJson = result.bodyJson();

            result.hasStatus(ENTITY_NOT_FOUND.getStatus());
            resultBodyJson.extractingPath("$.message").asString().contains("User not found with id");

        }

    }


    @Nested
    @DisplayName("사용자 등록 관련 테스트")
    class CreateTests {

        @Test
        @DisplayName("사용자 등록")
        void createUser() throws Exception {

            var userCreateRequestDto = new UserCreateRequestDto("newUser", "newUser@test.com", "test1234");

            var result = apiCallForPostMethod(BASE_URL, userCreateRequestDto);
            var resultBodyJson = result.bodyJson();

            result.hasStatus(HttpStatus.OK);

            resultBodyJson.extractingPath("$.name").isEqualTo(userCreateRequestDto.name());
            resultBodyJson.extractingPath("$.email").isEqualTo(userCreateRequestDto.email());
        }

        @Test
        @DisplayName("사용자 등록 - 잘못된 요청")
        void createUser_BadRequest() throws Exception {

            var invalidUserCreateRequestDto = new UserCreateRequestDto("", "invalidEmail", "");

            var result = apiCallForPostMethod(BASE_URL, invalidUserCreateRequestDto);
            var resultBodyJson = result.bodyJson();

            result.hasStatus(INVALID_INPUT_VALUE.getStatus());

            resultBodyJson.extractingPath("$.errors").isNotEmpty();
            resultBodyJson.extractingPath("$.message").asString().contains(INVALID_INPUT_VALUE.getMessage());

        }

        @Test
        @DisplayName("사용자 등록 - 이메일 중복")
        void createUser_EmailDuplicate() throws Exception {

            // 이미 존재하는 이메일로 사용자 등록 시도
            var existingEmail = users.getFirst().getEmail();
            var userCreateRequestDto = new UserCreateRequestDto("user1", existingEmail,"test1234");

            var result = apiCallForPostMethod(BASE_URL, userCreateRequestDto);
            var resultBodyJson = result.bodyJson();

            result.hasStatus(EMAIL_DUPLICATE.getStatus());

            resultBodyJson.extractingPath("$.message").asString().contains(EMAIL_DUPLICATE.getMessage());

        }
    }
}
