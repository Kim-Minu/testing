package com.example.testing.base;


import com.example.testing.global.config.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResultAssert;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Transactional
public class BaseControllerTest {
    @Autowired
    protected MockMvcTester mockMvcTester;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String accessToken;

    protected String refreshToken;

    private String tokenType;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() throws Exception{
        // 테스트에 필요한 초기화 작업을 수행합니다.
        // 예를 들어, MockMvcTester를 설정하거나, 테스트 데이터베이스를 초기화하는 등의 작업을 수행할 수 있습니다.

        // accessToken 발급 후 api test에 사용

        var email = "user3@test.com";

        this.accessToken = jwtTokenProvider.createAccessToken(email);
        this.refreshToken = jwtTokenProvider.createRefreshToken(email);
        this.tokenType = "Bearer";

    }

    /**
     * GET 요청을 위한 API 호출 메서드
     *
     * @param uri 요청 URI
     * @return JSON 응답 본문
     * @throws Exception 예외 발생 시
     */
    protected MvcTestResultAssert apiCallForGetMethod(String uri, Object... pathVariables) throws Exception {

        if (pathVariables == null) {
            pathVariables = new Object[]{}; // 기본값으로 빈 배열 설정
        }

        return mockMvcTester
                .get()
                .uri(uri, pathVariables)
                .header("Authorization", tokenType+" " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .assertThat()
                .apply(print());
    }

    protected MvcTestResultAssert apiCallForPostMethod(String uri, Object requestBody) throws Exception {
        return mockMvcTester
                .post()
                .uri(uri)
                .header("Authorization", tokenType+" " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .assertThat()
                .apply(print())
                
                ;
    }

}
