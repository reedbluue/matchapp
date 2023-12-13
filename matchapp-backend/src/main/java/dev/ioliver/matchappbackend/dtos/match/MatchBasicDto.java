package dev.ioliver.matchappbackend.dtos.match;

import dev.ioliver.matchappbackend.dtos.matchUserInfo.MatchUserInfoBasicDto;
import lombok.Builder;

@Builder
public record MatchBasicDto(

    Long id,

    MatchUserInfoBasicDto userInfo1,

    MatchUserInfoBasicDto userInfo2

) {
}
