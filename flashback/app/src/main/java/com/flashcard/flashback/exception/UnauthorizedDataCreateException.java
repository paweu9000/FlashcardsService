package com.flashcard.flashback.exception;

public class UnauthorizedDataCreateException extends RuntimeException{

    public UnauthorizedDataCreateException(Class<?> entity) {
        super("You are not authorized to create: " + entity.getSimpleName());
    }
}
