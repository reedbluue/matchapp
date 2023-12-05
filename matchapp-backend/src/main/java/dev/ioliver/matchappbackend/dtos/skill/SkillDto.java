package dev.ioliver.matchappbackend.dtos.skill;

import dev.ioliver.matchappbackend.models.SkillArea;
import lombok.Builder;

@Builder
public record SkillDto(

    Long id,

    String name,

    String description,

    SkillArea skillArea

) {
}
