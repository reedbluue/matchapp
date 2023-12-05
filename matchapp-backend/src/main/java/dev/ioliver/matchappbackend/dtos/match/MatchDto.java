package dev.ioliver.matchappbackend.dtos.match;

import dev.ioliver.matchappbackend.dtos.matchUserInfo.MatchUserInfoDto;
import lombok.Builder;

@Builder
public record MatchDto(

    Long id,

    MatchUserInfoDto userInfo1,

    MatchUserInfoDto userInfo2

) {
}
