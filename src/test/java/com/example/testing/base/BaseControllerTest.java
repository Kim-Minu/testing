package com.example.testing.base;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.json.AbstractJsonContentAssert;
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

    @BeforeEach
    public void setUp() {
        // 테스트에 필요한 초기화 작업을 수행합니다.
        // 예를 들어, MockMvcTester를 설정하거나, 테스트 데이터베이스를 초기화하는 등의 작업을 수행할 수 있습니다.



    }

    /**
     * GET 요청을 위한 API 호출 메서드
     *
     * @param uri 요청 URI
     * @return JSON 응답 본문
     * @throws Exception 예외 발생 시
     */
    protected AbstractJsonContentAssert<?> apiCallForGetMethod(String uri, HttpStatus httpStatus) throws Exception {
        return mockMvcTester
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .assertThat()
                .apply(print())
                .hasStatus(httpStatus)
                .bodyJson();
    }

    protected MvcTestResultAssert apiCallForPostMethod(String uri, Object requestBody) throws Exception {
        return mockMvcTester
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .assertThat()
                .apply(print())
                
                ;
    }

}
