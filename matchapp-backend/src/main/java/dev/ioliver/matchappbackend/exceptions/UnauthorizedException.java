package dev.ioliver.matchappbackend.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends DefaultException {
  public UnauthorizedException() {
    super("Unauthorized", HttpStatus.UNAUTHORIZED);
  }

  public UnauthorizedException(String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }
}
