package com.flashcard.flashback.exception;

public class UnauthorizedDataAccessException extends RuntimeException{

    public UnauthorizedDataAccessException(Class<?> entity) {
        super("You are not authorized to view entity: " + entity.getSimpleName());
    }
}
