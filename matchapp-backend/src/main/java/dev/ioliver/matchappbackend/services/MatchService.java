package dev.ioliver.matchappbackend.services;

import dev.ioliver.matchappbackend.dtos.match.MatchAcceptDto;
import dev.ioliver.matchappbackend.dtos.match.MatchBasicDto;
import dev.ioliver.matchappbackend.dtos.match.MatchDto;
import dev.ioliver.matchappbackend.dtos.match.MatchRejectDto;
import dev.ioliver.matchappbackend.dtos.matchUserInfo.MatchUserInfoBasicDto;
import dev.ioliver.matchappbackend.dtos.user.UserBasicDto;
import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.dtos.user.UserInfoDto;
import dev.ioliver.matchappbackend.dtos.userSkill.UserSkillSetDto;
import dev.ioliver.matchappbackend.enums.MatchResult;
import dev.ioliver.matchappbackend.enums.MatchStatus;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.mappers.MatchMapper;
import dev.ioliver.matchappbackend.mappers.SkillMapper;
import dev.ioliver.matchappbackend.mappers.UserMapper;
import dev.ioliver.matchappbackend.models.Match;
import dev.ioliver.matchappbackend.models.MatchUserInfo;
import dev.ioliver.matchappbackend.repositories.MatchRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchService {
  private final MatchRepository matchRepository;
  private final MatchMapper matchMapper;
  private final UserMapper userMapper;
  private final UserSkillService userSkillService;
  private final SkillMapper skillMapper;

  @Transactional
  public MatchResult acceptMatch(UserDto principalUser, MatchAcceptDto matchAcceptDto)
      throws BadRequestException {
    if (!userSkillService.checkIfSkillExistsToTheUser(matchAcceptDto.secondaryUserId(),
        matchAcceptDto.skillToLearnId(), true))
      throw new BadRequestException("The skills you are trying to learn not exists for the user!");

    boolean exists =
        matchRepository.checkIfMatchExists(principalUser.id(), matchAcceptDto.secondaryUserId());

    if (!exists) {
      Match entity = matchMapper.acceptToEntity(matchAcceptDto);

      entity.getUserInfo1().setUser(userMapper.toEntity(principalUser));
      entity.getUserInfo1().setStatus(MatchStatus.ACCEPTED);

      matchMapper.toDto(matchRepository.save(entity));

      return MatchResult.CREATED;
    } else {
      Optional<Match> match =
          matchRepository.findByUsers(principalUser.id(), matchAcceptDto.secondaryUserId());
      if (match.isEmpty()) throw new BadRequestException("This match is not exists!");
      if (match.get().getUserInfo2().getStatus() == MatchStatus.ACCEPTED)
        return MatchResult.CREATED;
      if (match.get().getUserInfo2().getStatus() == MatchStatus.REJECTED)
        return MatchResult.CREATED;
      if (match.get().getUserInfo1().getStatus() == MatchStatus.REJECTED)
        return MatchResult.CREATED;

      match.get().getUserInfo2().setStatus(MatchStatus.ACCEPTED);
      match.get()
          .getUserInfo2()
          .setSkillToLearn(skillMapper.toEntity(matchAcceptDto.skillToLearnId()));

      matchRepository.save(match.get());

      return MatchResult.ACCEPTED;
    }
  }

  @Transactional
  public void rejectMatch(UserDto principalUser, MatchRejectDto matchRejectDto)
      throws BadRequestException {
    boolean exists =
        matchRepository.checkIfMatchExists(principalUser.id(), matchRejectDto.secondaryUserId());

    if (!exists) {
      Match entity = matchMapper.rejectToEntity(matchRejectDto);

      if (entity.getUserInfo1() == null) entity.setUserInfo1(MatchUserInfo.builder().build());

      entity.getUserInfo1().setUser(userMapper.toEntity(principalUser));
      entity.getUserInfo1().setStatus(MatchStatus.REJECTED);

      matchMapper.toDto(matchRepository.save(entity));
    } else {
      Optional<Match> match =
          matchRepository.findByUsers(principalUser.id(), matchRejectDto.secondaryUserId());
      if (match.isEmpty()) throw new BadRequestException("This match is not exists!");
      if (match.get().getUserInfo2().getStatus() == MatchStatus.REJECTED) return;
      if (match.get().getUserInfo1().getStatus() == MatchStatus.REJECTED) return;

      match.get().getUserInfo2().setStatus(MatchStatus.REJECTED);

      matchRepository.save(match.get());
    }
  }

  @Transactional(readOnly = true)
  public List<MatchDto> listAllMatchesByUserId(Long id) {
    return matchMapper.toListDto(matchRepository.findAllByUserId(id));
  }

  @Transactional(readOnly = true)
  public List<MatchBasicDto> listAllAcceptedMatchesByUserId(Long id) {
    return matchRepository.findAllAcceptedByUserId(id).stream().map(m -> {
      return MatchBasicDto.builder()
          .userInfo1(MatchUserInfoBasicDto.builder()
              .id(m.getUserInfo1().getId())
              .user(UserBasicDto.builder()
                  .id(m.getUserInfo1().getUser().getId())
                  .fullName(m.getUserInfo1().getUser().getFullName())
                  .userSkillSet(UserSkillSetDto.builder()
                      .learnSkills(m.getUserInfo1()
                          .getUser()
                          .getSkillsToLearn()
                          .stream()
                          .map(skillMapper::toDto)
                          .toList())
                      .teachSkills(m.getUserInfo1()
                          .getUser()
                          .getSkillsToTeach()
                          .stream()
                          .map(skillMapper::toDto)
                          .toList())
                      .build())
                  .build())
              .status(m.getUserInfo1().getStatus())
              .skillToLearn(m.getUserInfo1().getSkillToLearn())
              .build())
          .userInfo2(MatchUserInfoBasicDto.builder()
              .id(m.getUserInfo2().getId())
              .user(UserBasicDto.builder()
                  .id(m.getUserInfo2().getUser().getId())
                  .fullName(m.getUserInfo2().getUser().getFullName())
                  .userSkillSet(UserSkillSetDto.builder()
                      .learnSkills(m.getUserInfo2()
                          .getUser()
                          .getSkillsToLearn()
                          .stream()
                          .map(skillMapper::toDto)
                          .toList())
                      .teachSkills(m.getUserInfo2()
                          .getUser()
                          .getSkillsToTeach()
                          .stream()
                          .map(skillMapper::toDto)
                          .toList())
                      .build())
                  .build())
              .status(m.getUserInfo2().getStatus())
              .skillToLearn(m.getUserInfo2().getSkillToLearn())
              .build())
          .build();
    }).toList();
  }

  @Transactional(readOnly = true)
  public MatchDto findById(Long id) throws BadRequestException {
    return matchMapper.toDto(matchRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("Match not found!")));
  }
}
