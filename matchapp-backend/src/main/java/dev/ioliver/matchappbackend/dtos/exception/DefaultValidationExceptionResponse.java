package dev.ioliver.matchappbackend.dtos.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public class DefaultValidationExceptionResponse extends DefaultExceptionResponse {
  private List<InvalidArgumentErrorDto> fieldsErrors = new ArrayList<>();

  public DefaultValidationExceptionResponse(MethodArgumentNotValidException e) {
    super(HttpStatus.BAD_REQUEST, "Some fields are invalid. Please check them.");

    if (e.getBindingResult() != null && e.getBindingResult().hasFieldErrors())
      for (var fieldError : e.getBindingResult().getFieldErrors()) {
        fieldsErrors.add(InvalidArgumentErrorDto.builder()
            .field(fieldError.getField())
            .message(fieldError.getDefaultMessage())
            .build());
      }
  }

  @Builder
  private record InvalidArgumentErrorDto(String field, String message) {
  }
}
