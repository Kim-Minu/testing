package com.example.testing.domain.user.controller;

import com.example.testing.base.BaseControllerTest;
import com.example.testing.domain.user.dto.UserCreateRequestDto;
import com.example.testing.domain.user.entity.User;
import com.example.testing.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.testing.global.exception.ErrorCode.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class UserControllerTest extends BaseControllerTest {

    @Autowired
    UserRepository userRepository;

    List<User> users;

    @BeforeEach
    public void setUp() {

        users = List.of(
                new User(null, "user1", "user1@test.com"),
                new User(null, "user2", "user2@test.com"),
                new User(null, "user3", "user3@test.com")
        );

        // ID를 수동으로 설정하지 않기
        // saveAll 호출 시 ID를 null로 설정하여 JPA가 자동으로 ID를 생성하도록 합니다.
        userRepository.saveAll(users);
    }


    @Test
    @DisplayName("모든 사용자 조회")
    void getAllUsers() {

        var result = mockMvcTester
                .get()
                .uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .assertThat()
                .apply(print())
                .hasStatusOk()
                .bodyJson();

        result.extractingPath("$.userList[0].username").isEqualTo(users.get(0).getUsername());
        result.extractingPath("$.userList[0].email").isEqualTo(users.get(0).getEmail());

        result.extractingPath("$.userList[1].username").isEqualTo(users.get(1).getUsername());
        result.extractingPath("$.userList[1].email").isEqualTo(users.get(1).getEmail());

    }


    @Nested
    @DisplayName("사용자 조회 관련 테스트")
    class RetrievalTests {

        @Test
        @DisplayName("사용자 ID로 조회")
        void getUserById() {

            Long userId = users.getFirst().getId();

            var result = mockMvcTester
                    .get()
                    .uri("/users/{userId}", userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .assertThat()
                    .apply(print())
                    .hasStatusOk()
                    .bodyJson();

            result.extractingPath("$.username").isEqualTo(users.getFirst().getUsername());
            result.extractingPath("$.email").isEqualTo(users.getFirst().getEmail());
        }

        @Test
        @DisplayName("사용자 ID로 조회 - 사용자 없음")
        void getUserById_NotFound() {

            Long nonExistentId = 999L;

            var result = mockMvcTester
                    .get()
                    .uri("/users/{userId}", nonExistentId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .assertThat()
                    .apply(print())
                    .hasStatus(ENTITY_NOT_FOUND.getStatus())
                    .bodyJson();

            result.extractingPath("$.message").asString().contains("User not found with id");
        }

    }


    @Nested
    @DisplayName("사용자 등록 관련 테스트")
    class CreateTests {

        @Test
        @DisplayName("사용자 등록")
        void createUser() throws JsonProcessingException {

            var userCreateRequestDto = new UserCreateRequestDto("newUser", "newUser@test.com");

            var result = mockMvcTester
                    .post()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userCreateRequestDto))
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .assertThat()
                    .apply(print())
                    .hasStatusOk()
                    .bodyJson();

            result.extractingPath("$.username").isEqualTo(userCreateRequestDto.username());
            result.extractingPath("$.email").isEqualTo(userCreateRequestDto.email());
        }

        @Test
        @DisplayName("사용자 등록 - 잘못된 요청")
        void createUser_BadRequest() throws JsonProcessingException {

            var invalidUserCreateRequestDto = new UserCreateRequestDto("", "invalidEmail");

            var result = mockMvcTester
                    .post()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidUserCreateRequestDto))
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .assertThat()
                    .apply(print())
                    .hasStatus(INVALID_INPUT_VALUE.getStatus())
                    .bodyJson();

            result.extractingPath("$.errors").isNotEmpty();
            result.extractingPath("$.message").asString().contains(INVALID_INPUT_VALUE.getMessage());

        }

        @Test
        @DisplayName("사용자 등록 - 이메일 중복")
        void createUser_EmailDuplicate() throws JsonProcessingException {

            // 이미 존재하는 이메일로 사용자 등록 시도
            var existingEmail = users.getFirst().getEmail();
            var userCreateRequestDto = new UserCreateRequestDto("user1", existingEmail);

            var result = mockMvcTester
                    .post()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userCreateRequestDto))
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .assertThat()
                    .apply(print())
                    .hasStatus(EMAIL_DUPLICATE.getStatus())
                    .bodyJson();

            result.extractingPath("$.message").asString().contains(EMAIL_DUPLICATE.getMessage());

        }
    }
}
