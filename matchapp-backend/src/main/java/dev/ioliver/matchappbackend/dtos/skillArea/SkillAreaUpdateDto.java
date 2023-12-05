package dev.ioliver.matchappbackend.dtos.skillArea;

import lombok.Builder;

@Builder
public record SkillAreaUpdateDto(

    Long id,

    String name,

    String description

) {
}
