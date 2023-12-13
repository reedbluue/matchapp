package dev.ioliver.matchappbackend.dtos.user;

import dev.ioliver.matchappbackend.dtos.userSkill.UserSkillSetDto;
import lombok.Builder;

@Builder
public record UserBasicDto(

    Long id,

    String fullName,

    UserSkillSetDto userSkillSet

) {
}
