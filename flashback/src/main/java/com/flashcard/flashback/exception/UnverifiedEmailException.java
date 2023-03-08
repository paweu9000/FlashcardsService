package com.flashcard.flashback.exception;

public class UnverifiedEmailException extends RuntimeException{

    public UnverifiedEmailException() {
        super("You have not verified your email");
    }
}
