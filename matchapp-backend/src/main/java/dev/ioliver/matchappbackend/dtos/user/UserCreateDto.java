package dev.ioliver.matchappbackend.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record UserCreateDto(@Email @NotEmpty @Length(max = 255) String email,

                            @NotEmpty @Length(min = 8, max = 255) String password,

                            @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate) {
}
