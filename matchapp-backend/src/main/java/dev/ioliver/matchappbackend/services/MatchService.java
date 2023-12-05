package dev.ioliver.matchappbackend.services;

import dev.ioliver.matchappbackend.dtos.match.MatchCreateDto;
import dev.ioliver.matchappbackend.dtos.match.MatchDto;
import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.enums.MatchStatus;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.mappers.MatchMapper;
import dev.ioliver.matchappbackend.mappers.UserMapper;
import dev.ioliver.matchappbackend.models.Match;
import dev.ioliver.matchappbackend.repositories.MatchRepository;
import java.util.List;
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

  public MatchDto create(UserDto principalUser, MatchCreateDto dto) throws BadRequestException {
    if (dto.skillToLearnId().equals(dto.skillToTeachId()))
      throw new BadRequestException("Skill to teach and skill to learn cannot be the same");

    if (userSkillService.checkIfSkillExistsToTheUser(principalUser.id(), dto.skillToTeachId(), true)
        || userSkillService.checkIfSkillExistsToTheUser(dto.secondaryUserId(), dto.skillToLearnId(),
        false)) throw new BadRequestException(
        "The skills you are trying to teach or learn not exists for these users!");

    boolean exists = matchRepository.checkIfMatchExists(principalUser.id(), dto.secondaryUserId(),
        dto.skillToTeachId(), dto.skillToLearnId());
    if (exists) throw new BadRequestException("Match already exists!");

    Match entity = matchMapper.createToEntity(dto);

    entity.getUserInfo1().setUser(userMapper.toEntity(principalUser));
    entity.getUserInfo1().setStatus(MatchStatus.ACCEPTED);

    return matchMapper.toDto(matchRepository.save(entity));
  }

  @Transactional
  public void acceptMatch(Long matchId, Long userId) throws BadRequestException {
    boolean isAcceptable = matchRepository.checkIfMatchIsAcceptable(matchId, userId);
    if (!isAcceptable) throw new BadRequestException("This match is not acceptable or not exists!");

    MatchDto matchDto = findById(matchId);
    Match entity = matchMapper.toEntity(matchDto);
    entity.getUserInfo2().setStatus(MatchStatus.ACCEPTED);

    matchRepository.save(entity);
  }

  @Transactional
  public void rejectMatch(Long matchId, Long userId) throws BadRequestException {
    boolean isAcceptable = matchRepository.checkIfMatchIsAcceptable(matchId, userId);
    if (!isAcceptable) throw new BadRequestException("This match is not rejectable or not exists!");

    MatchDto matchDto = findById(matchId);
    Match entity = matchMapper.toEntity(matchDto);
    entity.getUserInfo2().setStatus(MatchStatus.REJECTED);

    matchRepository.save(entity);
  }

  @Transactional(readOnly = true)
  public List<MatchDto> listAllMatchesByUserId(Long id) {
    return matchMapper.toListDto(matchRepository.findAllByUserId(id));
  }

  @Transactional(readOnly = true)
  public List<MatchDto> listAllAcceptedMatchesByUserId(Long id) {
    return matchMapper.toListDto(matchRepository.findAllAcceptedByUserId(id));
  }

  @Transactional(readOnly = true)
  public List<MatchDto> listAllWaitingMatchesByUserId(Long id) {
    return matchMapper.toListDto(matchRepository.findAllWaitingByUserId(id));
  }

  @Transactional(readOnly = true)
  public MatchDto findById(Long id) throws BadRequestException {
    return matchMapper.toDto(matchRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("Match not found!")));
  }
}
