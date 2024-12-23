package com.punna.eventcatalog.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.punna.commons.exception.EntityNotFoundException;
import org.punna.commons.exception.ProblemDetail;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.Locale;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ApplicationExceptionHandler {


    private final MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException e) {
        return Mono.just(ProblemDetail
                .builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build());
    }


    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ProblemDetail> handleWebExchangeBindException(WebExchangeBindException ex) {
        ProblemDetail problemDetail = new ProblemDetail();
        ex
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    problemDetail
                            .getErrors()
                            .put(fieldName, message);
                });
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setMessage(messageSource.getMessage("validation.invalid-body",
                null,
                Locale.getDefault()));
        return Mono.just(problemDetail);
    }


    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: "));
    }


}