package com.flashcard.flashback.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(Class<?> entity) {
        super(entity.getSimpleName().toLowerCase() + " does not exist");
    }

    public EntityNotFoundException(Long id, Class<?> entity) {
        super(entity.getSimpleName().toLowerCase() + " with id: '" + id + "' does not exist");
    }
}
