package dev.ioliver.matchappbackend.dtos.user;

import dev.ioliver.matchappbackend.dtos.auth.AuthInfoDto;
import dev.ioliver.matchappbackend.dtos.userSkill.UserSkillSetDto;
import lombok.Builder;

@Builder
public record UserInfoDto(

    AuthInfoDto authInfo,

    String profileImageUrl,

    UserSkillSetDto userSkillSet

) {
}
