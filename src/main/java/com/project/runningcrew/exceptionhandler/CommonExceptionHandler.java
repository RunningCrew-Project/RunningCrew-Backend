package com.project.runningcrew.exceptionhandler;

import com.project.runningcrew.exception.auth.AuthenticationException;
import com.project.runningcrew.exception.auth.AuthorizationException;
import com.project.runningcrew.exception.duplicate.DuplicateException;
import com.project.runningcrew.exception.badinput.BadInputException;
import com.project.runningcrew.exception.image.ImageException;
import com.project.runningcrew.exception.jwt.JwtExpiredException;
import com.project.runningcrew.exception.jwt.JwtInvalidException;
import com.project.runningcrew.exception.jwt.JwtVerificationException;
import com.project.runningcrew.exception.notFound.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
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
                .code(e.getErrorCode().getCode())
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
                .code(e.getErrorCode().getCode())
                .messages(e.getMessage())
                .errors(Map.of())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * JwtInvalidException 이 발생하면, ErrorResponse 와 HttpStatus.BAD_REQUEST 를 담은 ResponseEntity 를 반환
     *
     * @param e
     * @return ErrorResponse 와 HttpStatus.BAD_REQUEST 를 담은 ResponseEntity
     */
    @ExceptionHandler(JwtInvalidException.class)
    public ResponseEntity<ErrorResponse> JwtInvalidExceptionExceptionHandler(JwtInvalidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getErrorCode().getCode())
                .messages(e.getMessage())
                .errors(Map.of())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
                .code(e.getErrorCode().getCode())
                .messages(e.getMessage())
                .errors(Map.of())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * BindException 이 발생하면, ErrorResponse 와 HttpStatus.BAD_REQUEST 를 담은 ResponseEntity 를 반환
     * @param e
     * @return ErrorResponse 와 HttpStatus.BAD_REQUEST 를 담은 ResponseEntity
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> BindExceptionHandler(BindException e) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("COM_01")
                .messages("유효성 검사를 실패하였습니다.")
                .errors(errors)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * AuthenticationException 이 발생하면, ErrorResponse 와 HttpStatus.UNAUTHORIZED 를 담은 ResponseEntity 를 반환
     *
     * @param e
     * @return ErrorResponse 와 HttpStatus.UNAUTHORIZED 를 담은 ResponseEntity
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> AuthenticationExceptionHandler(AuthenticationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getErrorCode().getCode())
                .messages(e.getMessage())
                .errors(Map.of())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * AuthorizationException 이 발생하면, ErrorResponse 와 HttpStatus.FORBIDDEN 를 담은 ResponseEntity 를 반환
     *
     * @param e
     * @return ErrorResponse 와 HttpStatus.FORBIDDEN 를 담은 ResponseEntity
     */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> AuthorizationExceptionHandler(AuthorizationException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getErrorCode().getCode())
                .messages(e.getMessage())
                .errors(Map.of())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * DuplicateException 이 발생하면, ErrorResponse 와 HttpStatus.CONFLICT 를 담은 ResponseEntity 를 반환
     *
     * @param e
     * @return ErrorResponse 와 HttpStatus.CONFLICT 를 담은 ResponseEntity
     */
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> AlreadyExistsExceptionHandler(DuplicateException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put(e.getType(), e.getValue());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getErrorCode().getCode())
                .messages(e.getMessage())
                .errors(errors)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * BadInputException 이 발생하면, ErrorResponse 와 HttpStatus.BAD_REQUEST 를 담은 ResponseEntity 를 반환
     *
     * @param e
     * @return ErrorResponse 와 HttpStatus.BAD_REQUEST 를 담은 ResponseEntity
     */
    @ExceptionHandler(BadInputException.class)
    public ResponseEntity<ErrorResponse> BadInputExceptionHandler(BadInputException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getErrorCode().getCode())
                .messages(e.getMessage())
                .errors(e.getBadInputMaps())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * ImageException 이 발생하면, ErrorResponse 와 HttpStatus.BAD_REQUEST 를 담은 ResponseEntity 를 반환
     *
     * @param e
     * @return ErrorResponse 와 HttpStatus.BAD_REQUEST 를 담은 ResponseEntity
     */
    @ExceptionHandler(ImageException.class)
    public ResponseEntity<ErrorResponse> ImageExceptionHandler(ImageException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getErrorCode().getCode())
                .messages(e.getMessage())
                .errors(e.getErrors())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
