package com.example.testing.domain.user.exception;

import com.example.testing.global.exception.ErrorCode;
import com.example.testing.global.exception.InvalidValueException;

public class UserSignInException extends InvalidValueException {
    private final String username;

    public UserSignInException(String username) {
        super(ErrorCode.USER_SIGN_IN_FAILED.getMessage(), ErrorCode.USER_SIGN_IN_FAILED);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
