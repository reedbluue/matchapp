package dev.ioliver.matchappbackend.dtos.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record RefreshRequestDto(

    @NotEmpty String refreshToken

) {
}
