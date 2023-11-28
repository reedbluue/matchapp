package dev.ioliver.matchappbackend.dtos.user;

import dev.ioliver.matchappbackend.enums.Role;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDto(UUID id,

                      String email,

                      String hashedPassword,

                      String username,

                      boolean emailVerified,

                      boolean active,

                      LocalDate birthDate,

                      Instant createdAt,

                      List<Role> roles) {
}
