package com.example.testing.domain.user.exception;

import com.example.testing.global.exception.BusinessException;
import com.example.testing.global.exception.ErrorCode;
import lombok.Getter;


@Getter
public class EmailDuplicateException extends BusinessException {

    private final String email;

    public EmailDuplicateException(String email) {
        super(ErrorCode.EMAIL_DUPLICATE.getMessage() + email, ErrorCode.EMAIL_DUPLICATE);
        this.email = email;
    }

}
