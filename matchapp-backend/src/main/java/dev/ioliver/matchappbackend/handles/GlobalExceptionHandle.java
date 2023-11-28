package dev.ioliver.matchappbackend.handles;

import dev.ioliver.matchappbackend.dtos.exception.DefaultExceptionResponse;
import dev.ioliver.matchappbackend.dtos.exception.DefaultValidationExceptionResponse;
import dev.ioliver.matchappbackend.exceptions.DefaultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<DefaultValidationExceptionResponse> methodArgumentNotValid(
      MethodArgumentNotValidException e) {
    return ResponseEntity.status(e.getStatusCode()).body(new DefaultValidationExceptionResponse(e));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<DefaultExceptionResponse> authenticationException(
      HttpMessageNotReadableException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new DefaultExceptionResponse(HttpStatus.BAD_REQUEST,
            e.getMessage().contains("Required request body is missing")
                ? "Required request body is missing." : e.getMessage()));
  }

  @ExceptionHandler(DefaultException.class)
  public ResponseEntity<DefaultExceptionResponse> defaultException(DefaultException e) {
    return ResponseEntity.status(e.getStatus())
        .body(new DefaultExceptionResponse(e.getStatus(), e.getMessage()));
  }
}
