package com.project.runningcrew.exceptionhandler;

import com.project.runningcrew.exception.jwt.JwtExpiredException;
import com.project.runningcrew.exception.jwt.JwtVerificationException;
import com.project.runningcrew.exception.notFound.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CommonExceptionHandler {

    /**
     * ResourceNotFoundException 이 발생하면, ErrorResponse 와 HttpStatus.NOT_FOUND 를 담은 ResponseEntity 를 반환
     *
     * @param e
     * @return ErrorResponse 와 HttpStatus.NOT_FOUND 가 담긴 ResponseEntity
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> ResourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .messages(e.getMessage())
                .errors(Map.of())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * JwtExpiredException 이 발생하면, ErrorResponse 와 HttpStatus.UNAUTHORIZED 를 담은 ResponseEntity 를 반환
     *
     * @param e
     * @return ErrorResponse 와 HttpStatus.UNAUTHORIZED 를 담은 ResponseEntity
     */
    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<ErrorResponse> JwtExpiredExceptionHandler(JwtExpiredException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .messages(e.getMessage())
                .errors(Map.of())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * JwtVerificationException 이 발생하면, ErrorResponse 와 HttpStatus.FORBIDDEN 를 담은 ResponseEntity 를 반환
     *
     * @param e
     * @return ErrorResponse 와 HttpStatus.FORBIDDEN 를 담은 ResponseEntity
     */
    @ExceptionHandler(JwtVerificationException.class)
    public ResponseEntity<ErrorResponse> JwtVerificationExceptionHandler(JwtVerificationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .messages(e.getMessage())
                .errors(Map.of())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }


}
