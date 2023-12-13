package dev.ioliver.matchappbackend.services;

import dev.ioliver.matchappbackend.dtos.skill.SkillDto;
import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.dtos.userSkill.UserSkillSetDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.mappers.SkillMapper;
import dev.ioliver.matchappbackend.mappers.UserMapper;
import dev.ioliver.matchappbackend.models.Skill;
import dev.ioliver.matchappbackend.models.User;
import dev.ioliver.matchappbackend.models.UserSkill;
import dev.ioliver.matchappbackend.repositories.UserSkillRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSkillService {
  private final UserSkillRepository userSkillRepository;
  private final SkillMapper skillMapper;
  private final UserMapper userMapper;

  @Transactional(readOnly = true)
  public UserSkillSetDto getUserSkills(Long userId) {
    List<UserSkill> userSkills = userSkillRepository.findAllByUser_Id(userId);
    List<SkillDto> learnSkills = new ArrayList<>();
    List<SkillDto> teachSkills = new ArrayList<>();
    for (var us : userSkills) {
      if (Boolean.TRUE.equals(us.getIsTeachingSkill()))
        teachSkills.add(skillMapper.toDto(us.getSkill()));
      else learnSkills.add(skillMapper.toDto(us.getSkill()));
    }
    return UserSkillSetDto.builder().learnSkills(learnSkills).teachSkills(teachSkills).build();
  }

  @Transactional
  public void updateTeachSkills(UserDto user, List<Long> skillsIds) throws BadRequestException {
    if (skillsIds.size() > 3) throw new BadRequestException("You can't teach more than 3 skills");

    User userEntity = userMapper.toEntity(user);

    for (Long skillId : skillsIds) {
      boolean exists =
          userSkillRepository.existsBySkill_IdAndUser_IdAndIsTeachingSkill(skillId, user.id(),
              false);
      if (exists) throw new BadRequestException(
          "This skill is already saved as an learning skill to this user");
    }

    List<Skill> skillList = skillMapper.toListEntity(skillsIds);
    List<UserSkill> entityList = skillList.stream()
        .map(skill -> UserSkill.builder()
            .user(userEntity)
            .skill(skill)
            .isTeachingSkill(true)
            .build())
        .toList();

    userSkillRepository.deleteAllByUser_IdAndIsTeachingSkill(user.id(), true);

    if (entityList.isEmpty()) return;
    userSkillRepository.saveAll(entityList);
  }

  @Transactional
  public void updateLearnSkills(UserDto user, List<Long> skillsIds) throws BadRequestException {
    if (skillsIds.size() > 3) throw new BadRequestException("You can't learn more than 3 skills");

    User userEntity = userMapper.toEntity(user);

    for (Long skillId : skillsIds) {
      boolean exists =
          userSkillRepository.existsBySkill_IdAndUser_IdAndIsTeachingSkill(skillId, user.id(),
              true);
      if (exists) throw new BadRequestException(
          "This skill is already saved as an teaching skill to this user");
    }

    List<Skill> skillList = skillMapper.toListEntity(skillsIds);
    List<UserSkill> entityList = skillList.stream()
        .map(skill -> UserSkill.builder()
            .user(userEntity)
            .skill(skill)
            .isTeachingSkill(false)
            .build())
        .toList();

    userSkillRepository.deleteAllByUser_IdAndIsTeachingSkill(user.id(), false);

    if (entityList.isEmpty()) return;
    userSkillRepository.saveAll(entityList);
  }

  public boolean checkIfSkillExistsToTheUser(Long userId, Long skillId, boolean isTeachingSkill) {
    return userSkillRepository.existsBySkill_IdAndUser_IdAndIsTeachingSkill(skillId, userId,
        isTeachingSkill);
  }
}