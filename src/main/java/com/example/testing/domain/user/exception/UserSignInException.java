package com.example.testing.domain.user.exception;

import com.example.testing.global.exception.ErrorCode;
import com.example.testing.global.exception.InvalidValueException;

public class UserSignInException extends InvalidValueException {
    private final String message;

    public UserSignInException(String message) {
        super(message, ErrorCode.USER_SIGN_IN_FAILED);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
