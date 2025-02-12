package com.punna.eventcatalog.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.EventApplicationException;
import org.punna.commons.exception.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import reactor.core.publisher.Mono;


@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException e) {
        return Mono.just(
            ProblemDetail.builder().message(e.getMessage()).status(HttpStatus.NOT_FOUND.value())
                .build());
    }

    @ExceptionHandler(EventApplicationException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleEventApplicationException(
        EventApplicationException e) {
        return Mono.just(ResponseEntity.status(e.getStatus())
            .body(ProblemDetail.builder().message(e.getMessage()).status(e.getStatus()).build()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ProblemDetail> handleWebExchangeBindException(WebExchangeBindException ex) {
        ProblemDetail problemDetail = new ProblemDetail();
        ex.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            problemDetail.getErrors().put(fieldName, message);
        });
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setMessage("Invalid Body");
        return Mono.just(problemDetail);
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Mono<ProblemDetail> handleAccessDeniedException(AccessDeniedException e) {
        return Mono.just(
            ProblemDetail.builder().status(HttpStatus.FORBIDDEN.value()).message(e.getMessage())
                .build());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ProblemDetail> handleConstraintViolationException(ConstraintViolationException e) {
        ProblemDetail problemDetail = new ProblemDetail();
        e.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String message = error.getMessage();
            problemDetail.getErrors().put(fieldName, message);
        });
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setMessage("Invalid Body");
        return Mono.just(problemDetail);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ProblemDetail> handleNoResourceFoundException(NoResourceFoundException e) {
        return Mono.just(ProblemDetail.builder()
            .message("Resource not found")
            .status(HttpStatus.NOT_FOUND.value())
            .build());
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An unexpected error occurred: "));
    }

}
