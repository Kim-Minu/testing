package com.example.testing.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        List<FieldErrorResponse> fieldErrorResponses = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach( it -> {
            fieldErrorResponses.add(
                    FieldErrorResponse.builder()
                            .filed(it.getField())
                            .value(Objects.requireNonNull(it.getRejectedValue()).toString())
                            .reason(it.getDefaultMessage())
                        .build()
            );
        });

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(ErrorCode.INVALID_INPUT_VALUE.getMessage())
                        .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                        .errors(fieldErrorResponses)
                        .code(ErrorCode.INVALID_INPUT_VALUE.getCode())
                    .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {

        List<FieldErrorResponse> fieldErrorResponses = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach( it -> {
            fieldErrorResponses.add(
                    FieldErrorResponse.builder()
                            .filed(it.getField())
                            .value(Objects.requireNonNull(it.getRejectedValue()).toString())
                            .reason(it.getDefaultMessage())
                        .build()
            );
        });

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(ErrorCode.INVALID_INPUT_VALUE.getMessage())
                        .status(ErrorCode.INVALID_INPUT_VALUE.getStatus())
                        .errors(fieldErrorResponses)
                        .code(ErrorCode.INVALID_INPUT_VALUE.getCode())
                    .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .code(e.getErrorCode())
                    .build(),
                HttpStatus.BAD_REQUEST
        );

    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException (HttpRequestMethodNotSupportedException e) {

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(ErrorCode.METHOD_NOT_ALLOWED.getMessage())
                        .status(ErrorCode.METHOD_NOT_ALLOWED.getStatus())
                        .code(ErrorCode.METHOD_NOT_ALLOWED.getCode())
                        .build(),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException (AccessDeniedException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(ErrorCode.HANDLE_ACCESS_DENIED.getMessage())
                        .status(ErrorCode.HANDLE_ACCESS_DENIED.getStatus())
                        .code(ErrorCode.HANDLE_ACCESS_DENIED.getCode())
                    .build(),
                HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.getStatus())
        );
    }


    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(e.getMessage().isBlank() ? e.getErrorCode().getMessage() : e.getMessage())
                        .status(e.getErrorCode().getStatus())
                        .code(e.getErrorCode().getCode())
                    .build(),
                HttpStatus.valueOf(e.getErrorCode().getStatus())
        );
    }

    /**
     * 서버 에러 처리
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(e.getMessage())
                        .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                        .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                    .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handle404(NoHandlerFoundException e) {

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message( "존재하지 않는 URL입니다. : " + e.getRequestURL())
                        .status(ErrorCode.NOT_FOUND_URL.getStatus())
                        .code(ErrorCode.NOT_FOUND_URL.getCode())
                    .build(),
                HttpStatus.NOT_FOUND
        );

    }


}
