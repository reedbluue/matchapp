package dev.ioliver.matchappbackend.dtos.auth;

import dev.ioliver.matchappbackend.enums.Role;
import java.util.List;
import lombok.Builder;

@Builder
public record AuthInfoDto(

    Long id,

    String email,

    String username,

    List<Role> roles

) {
}
