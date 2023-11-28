package dev.ioliver.matchappbackend.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DefaultException extends Exception {
  private final HttpStatus status;

  public DefaultException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
