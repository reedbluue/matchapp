package dev.ioliver.matchappbackend.dtos.skillArea;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record SkillAreaDto(

    Long id,

    @NotEmpty @Length(max = 255) String name,

    @Length(max = 255) String description

) {
}
