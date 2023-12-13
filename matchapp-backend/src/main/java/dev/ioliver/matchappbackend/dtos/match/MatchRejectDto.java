package dev.ioliver.matchappbackend.dtos.match;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MatchRejectDto(

    @NotNull Long secondaryUserId

) {
}
