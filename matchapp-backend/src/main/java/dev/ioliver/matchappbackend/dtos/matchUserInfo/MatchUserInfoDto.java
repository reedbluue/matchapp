package dev.ioliver.matchappbackend.dtos.matchUserInfo;

import dev.ioliver.matchappbackend.enums.MatchStatus;
import dev.ioliver.matchappbackend.models.Skill;
import dev.ioliver.matchappbackend.models.User;

public record MatchUserInfoDto(

    Long id,

    User user,

    MatchStatus status,

    Skill skillToTeach

) {
}
