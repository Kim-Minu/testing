package com.example.testing.base;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.json.AbstractJsonContentAssert;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
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

}
