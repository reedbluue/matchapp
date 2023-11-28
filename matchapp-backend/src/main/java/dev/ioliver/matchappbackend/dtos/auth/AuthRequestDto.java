package dev.ioliver.matchappbackend.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record AuthRequestDto(

    @NotEmpty @Email @Length(max = 255) String email,

    @NotEmpty @Length(max = 255) String password) {

}
