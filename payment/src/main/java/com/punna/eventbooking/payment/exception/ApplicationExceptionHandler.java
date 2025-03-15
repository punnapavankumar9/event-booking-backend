package com.punna.eventbooking.payment.exception;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import com.punna.commons.exception.EntityNotFoundException;
import com.punna.commons.exception.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ProblemDetail handleEntityNotFoundException(EntityNotFoundException e) {
    return ProblemDetail.builder().message(e.getMessage()).status(HttpStatus.NOT_FOUND.value())
        .build();
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ProblemDetail handleAccessDeniedException(AccessDeniedException e) {
    return ProblemDetail.builder().message(e.getMessage())
        .status(HttpStatus.UNAUTHORIZED.value()).build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProblemDetail handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    ProblemDetail problemDetail = new ProblemDetail();
    Map<String, Object> errors = problemDetail.getErrors();
    exception.getAllErrors().forEach(error -> {
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
