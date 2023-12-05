package dev.ioliver.matchappbackend.dtos.skill;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record SkillCreateDto(

    @NotEmpty @Length(max = 255) String name,

    @NotEmpty @Length(max = 255) String description,

    @NotNull Long skillAreaId

) {
}
