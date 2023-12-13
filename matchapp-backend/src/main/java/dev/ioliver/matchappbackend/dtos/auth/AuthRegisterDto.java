package dev.ioliver.matchappbackend.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record AuthRegisterDto(

    @Email @NotEmpty @Length(max = 255) String email,

    @NotEmpty @Length(min = 8, max = 255) String password,

    @NotEmpty @Length(min = 3, max = 255) String fullName,

    @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate

) {
}
