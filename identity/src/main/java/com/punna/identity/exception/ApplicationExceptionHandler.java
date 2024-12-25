package com.punna.identity.exception;

import lombok.extern.slf4j.Slf4j;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException e) {
        return ProblemDetail
                .builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = new ProblemDetail();
        Map<String, Object> errors = problemDetail.getErrors();
        exception
                .getAllErrors()
                .forEach(error -> {
                    errors.put(((FieldError) error).getField(), error.getDefaultMessage());
                });
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setMessage("Request Body Validation failed");
        return problemDetail;
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return "An unexpected error occurred: ";
    }
}
