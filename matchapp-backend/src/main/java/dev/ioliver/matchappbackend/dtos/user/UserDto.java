package dev.ioliver.matchappbackend.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.ioliver.matchappbackend.enums.Role;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record UserDto(

    Long id,

    String email,

    @JsonIgnore String hashedPassword,

    String username,

    boolean emailVerified,

    boolean active,

    LocalDate birthDate,

    Instant createdAt,

    List<Role> roles

) {
}
