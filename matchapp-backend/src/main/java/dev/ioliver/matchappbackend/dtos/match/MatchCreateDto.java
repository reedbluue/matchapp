package dev.ioliver.matchappbackend.dtos.match;

import jakarta.validation.constraints.NotNull;

public record MatchCreateDto(

    @NotNull Long secondaryUserId,

    @NotNull Long skillToTeachId,

    @NotNull Long skillToLearnId

) {
}
