package com.example.testing.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(400, "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    HANDLE_ACCESS_UNAUTHORIZED(401, "C006", "Access is UNAUTHORIZED"),
    NOT_FOUND_URL(404, "C006", "Invalid URL"),


    EMAIL_DUPLICATE(409, "M001", "이미 사용 중인 이메일입니다.")

    ;
    private int status;
    private String code;
    private String message;

}
