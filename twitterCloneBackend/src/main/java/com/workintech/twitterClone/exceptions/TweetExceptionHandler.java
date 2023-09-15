package com.workintech.twitterClone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TweetExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<TweetErrorResponse> handleException (TweetException exception) {
        TweetErrorResponse response = new TweetErrorResponse(exception.getStatus().value(),
                exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response,exception.getStatus());

    }
    @ExceptionHandler
    public ResponseEntity<TweetErrorResponse> handleException (Exception exception) {
        TweetErrorResponse response = new TweetErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);

    }
}
