package com.flashcard.flashback.exception;

public class SavedCollectionDuplicateException extends RuntimeException{

    public SavedCollectionDuplicateException(Long id) {
        super("Collection with id: " + id + " is already present in the saved collections!");
    }
}
