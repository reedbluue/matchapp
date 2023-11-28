package dev.ioliver.matchappbackend.dtos.auth;

import lombok.Builder;

@Builder
public record AuthResponseDto(

    String accessToken,

    String refreshToken

) {
}
