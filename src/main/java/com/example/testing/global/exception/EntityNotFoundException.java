package com.example.testing.global.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EntityNotFoundException extends BusinessException {

    private String message;

    public EntityNotFoundException(String message){
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }

}
