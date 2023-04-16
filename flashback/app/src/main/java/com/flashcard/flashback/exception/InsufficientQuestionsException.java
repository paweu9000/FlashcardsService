package com.flashcard.flashback.exception;

public class InsufficientQuestionsException extends RuntimeException{

    public InsufficientQuestionsException(Long id) {
        super("The collection with id: " + id + " does not meet the minimal(4) number of cards to create test");
    }
}
