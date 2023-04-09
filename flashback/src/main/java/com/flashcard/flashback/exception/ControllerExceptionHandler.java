package com.flashcard.flashback.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    ResponseEntity<Object> handleEntityNotFoundException(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(List.of(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnauthorizedDataDeleteException.class, UnauthorizedDataCreateException.class,
            UnauthorizedDataAccessException.class, UnverifiedEmailException.class})
    ResponseEntity<Object> handleUnauthorizedDataActionException(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(List.of(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({SavedCollectionDuplicateException.class})
    ResponseEntity<Object> handleSavedCollectionDuplicateException(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(List.of(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));
        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
    }
}
