package com.example.testing.domain.auth.controller;

import com.example.testing.base.BaseControllerTest;
import com.example.testing.domain.auth.dto.LoginRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.testing.global.exception.ErrorCode.USER_SIGN_IN_FAILED;

class AuthControllerTest extends BaseControllerTest {

    private final String BASE_URL = "/api/v1/auth";

    @Test
    @DisplayName("로그인 실패")
    void login_fail() throws Exception {

        var loginRequestDto = new LoginRequestDto("test@test.com", "password1234");

        var result = apiCallForPostMethod( BASE_URL + "/signIn", loginRequestDto);

        var resultBodyJson = result.bodyJson();

        result.hasStatus(USER_SIGN_IN_FAILED.getStatus());

        resultBodyJson.extractingPath("$.code").isEqualTo(USER_SIGN_IN_FAILED.getCode());
        resultBodyJson.extractingPath("$.status").isEqualTo(USER_SIGN_IN_FAILED.getStatus());

    }


}