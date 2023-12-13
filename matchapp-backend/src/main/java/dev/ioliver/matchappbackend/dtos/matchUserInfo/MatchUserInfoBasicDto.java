package dev.ioliver.matchappbackend.dtos.matchUserInfo;

import dev.ioliver.matchappbackend.dtos.user.UserBasicDto;
import dev.ioliver.matchappbackend.enums.MatchStatus;
import dev.ioliver.matchappbackend.models.Skill;
import lombok.Builder;

@Builder
public record MatchUserInfoBasicDto(

    Long id,

    UserBasicDto user,

    MatchStatus status,

    Skill skillToLearn

) {
}
