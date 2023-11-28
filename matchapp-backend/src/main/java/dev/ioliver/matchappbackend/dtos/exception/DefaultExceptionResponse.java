package dev.ioliver.matchappbackend.dtos.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class DefaultExceptionResponse {
  @JsonIgnore private final HttpStatus status;

  private final String message;

  private int statusCode;

  private String statusName;

  public DefaultExceptionResponse(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
    this.statusCode = status.value();
    this.statusName = status.name();
  }
}
