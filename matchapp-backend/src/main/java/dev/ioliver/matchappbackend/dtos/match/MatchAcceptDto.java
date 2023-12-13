package dev.ioliver.matchappbackend.dtos.match;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MatchAcceptDto(

    @NotNull Long secondaryUserId,

    @NotNull Long skillToLearnId

) {
}
