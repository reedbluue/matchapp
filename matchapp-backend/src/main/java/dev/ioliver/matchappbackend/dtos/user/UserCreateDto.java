package dev.ioliver.matchappbackend.dtos.user;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserCreateDto(

    String email,

    String fullName,

    String password,

    LocalDate birthDate

) {
}
