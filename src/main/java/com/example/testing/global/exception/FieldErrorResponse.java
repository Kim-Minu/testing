package com.example.testing.global.exception;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FieldErrorResponse {
    private String filed;
    private String value;
    private String reason;
}
