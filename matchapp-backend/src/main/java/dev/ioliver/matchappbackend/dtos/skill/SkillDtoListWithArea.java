package dev.ioliver.matchappbackend.dtos.skill;

import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaDto;
import java.util.List;
import lombok.Builder;

@Builder
public record SkillDtoListWithArea(

    SkillAreaDto skillArea,

    List<SkillDto> skills

) {
}
