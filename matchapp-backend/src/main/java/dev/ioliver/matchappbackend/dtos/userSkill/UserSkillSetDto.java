package dev.ioliver.matchappbackend.dtos.userSkill;

import dev.ioliver.matchappbackend.dtos.skill.SkillDto;
import java.util.List;
import lombok.Builder;

@Builder
public record UserSkillSetDto(

    List<SkillDto> learnSkills,

    List<SkillDto> teachSkills

) {
}
