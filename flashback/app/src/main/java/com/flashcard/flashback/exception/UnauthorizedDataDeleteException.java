package com.flashcard.flashback.exception;

public class UnauthorizedDataDeleteException extends RuntimeException{

    public UnauthorizedDataDeleteException(Class<?> deletion) {
        super("You are not authorized to delete: " + deletion.getSimpleName());
    }
}
