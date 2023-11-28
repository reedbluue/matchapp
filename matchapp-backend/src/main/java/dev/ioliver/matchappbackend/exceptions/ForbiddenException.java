package dev.ioliver.matchappbackend.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends DefaultException {
  public ForbiddenException() {
    super("Forbidden", HttpStatus.FORBIDDEN);
  }

  public ForbiddenException(String message) {
    super(message, HttpStatus.FORBIDDEN);
  }
}
