package com.example.testing.global.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EntityNotFoundException extends BusinessException {

    private String message;

    public EntityNotFoundException(String message){
        super(message, ErrorCode.ENTITY_NOT_FOUND);
        this.message = message;
    }

}
