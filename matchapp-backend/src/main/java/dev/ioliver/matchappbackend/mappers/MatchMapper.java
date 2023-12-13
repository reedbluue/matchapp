package dev.ioliver.matchappbackend.mappers;

import dev.ioliver.matchappbackend.dtos.match.MatchAcceptDto;
import dev.ioliver.matchappbackend.dtos.match.MatchBasicDto;
import dev.ioliver.matchappbackend.dtos.match.MatchDto;
import dev.ioliver.matchappbackend.dtos.match.MatchRejectDto;
import dev.ioliver.matchappbackend.models.Match;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SkillMapper.class, UserMapper.class})
public abstract class MatchMapper {

  public abstract MatchDto toDto(Match match);

  public abstract Match toEntity(MatchDto matchDto);

  @Mapping(source = "secondaryUserId", target = "userInfo2.user")
  @Mapping(source = "skillToLearnId", target = "userInfo1.skillToLearn")
  public abstract Match acceptToEntity(MatchAcceptDto matchAcceptDto);

  @Mapping(source = "secondaryUserId", target = "userInfo2.user")
  public abstract Match rejectToEntity(MatchRejectDto matchRejectDto);

  public abstract List<MatchDto> toListDto(List<Match> matches);

  public abstract List<MatchBasicDto> toBasicListDto(List<Match> matches);
}
