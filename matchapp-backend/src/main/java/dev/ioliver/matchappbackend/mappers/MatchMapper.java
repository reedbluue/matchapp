package dev.ioliver.matchappbackend.mappers;

import dev.ioliver.matchappbackend.dtos.match.MatchCreateDto;
import dev.ioliver.matchappbackend.dtos.match.MatchDto;
import dev.ioliver.matchappbackend.models.Match;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SkillMapper.class, UserMapper.class})
public abstract class MatchMapper {

  public abstract MatchDto toDto(Match match);

  public abstract Match toEntity(MatchDto matchDto);

  @Mapping(source = "secondaryUserId", target = "userInfo2.user")
  @Mapping(source = "skillToTeachId", target = "userInfo1.skillToTeach")
  @Mapping(source = "skillToLearnId", target = "userInfo2.skillToTeach")
  public abstract Match createToEntity(MatchCreateDto matchCreateDto);

  public abstract List<MatchDto> toListDto(List<Match> matches);
}
