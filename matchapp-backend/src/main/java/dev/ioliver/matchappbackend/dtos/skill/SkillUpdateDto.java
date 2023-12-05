package dev.ioliver.matchappbackend.dtos.skill;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SkillUpdateDto(

    @NotNull Long id,

    @NotEmpty @Length(max = 255) String name,

    @NotEmpty @Length(max = 255) String description,

    @NotNull Long skillAreaId

) {
}
