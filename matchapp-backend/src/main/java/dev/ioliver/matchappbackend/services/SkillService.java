package dev.ioliver.matchappbackend.services;

import dev.ioliver.matchappbackend.dtos.skill.SkillCreateDto;
import dev.ioliver.matchappbackend.dtos.skill.SkillDto;
import dev.ioliver.matchappbackend.dtos.skill.SkillDtoListWithArea;
import dev.ioliver.matchappbackend.dtos.skill.SkillUpdateDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.mappers.SkillMapper;
import dev.ioliver.matchappbackend.models.Skill;
import dev.ioliver.matchappbackend.repositories.SkillRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SkillService {
  private final SkillAreaService skillAreaService;
  private final SkillRepository skillRepository;
  private final SkillMapper skillMapper;

  @Transactional(readOnly = true)
  public List<SkillDto> findAll() {
    List<Skill> all = skillRepository.findAll();
    return skillMapper.toListDto(all);
  }

  @Transactional(readOnly = true)
  public List<SkillDtoListWithArea> findAllSeparatedByArea() {
    List<SkillDtoListWithArea> list = new ArrayList<>();
    List<SkillAreaDto> skillAreaDtoList = skillAreaService.findAll();
    for (SkillAreaDto skillAreaDto : skillAreaDtoList) {
      List<Skill> skills = skillRepository.findBySkillArea_Id(skillAreaDto.id());
      if (!skills.isEmpty()) list.add(SkillDtoListWithArea.builder()
          .skillArea(skillAreaDto)
          .skills(skillMapper.toListDto(skills))
          .build());
    }
    return list;
  }

  @Transactional(readOnly = true)
  public SkillDto findById(Long id) throws BadRequestException {
    Skill skill = skillRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("Don't exist a Skill with the id: " + id));
    return skillMapper.toDto(skill);
  }

  @Transactional
  public SkillDto create(SkillCreateDto dto) throws BadRequestException {
    if (skillRepository.existsByName(dto.name()))
      throw new BadRequestException("Already exists a Skill with the name: " + dto.name());

    Skill entity = skillMapper.createToEntity(dto);

    Skill saved = skillRepository.save(entity);
    return skillMapper.toDto(saved);
  }

  @Transactional
  public SkillDto update(SkillUpdateDto dto) throws BadRequestException {
    SkillDto skillDto = findById(dto.id());
    Skill entity = skillMapper.toEntity(skillDto);
    skillMapper.updateToEntity(dto, entity);

    Skill saved = skillRepository.save(entity);
    return skillMapper.toDto(saved);
  }

  @Transactional
  public void deleteById(Long id) throws BadRequestException {
    SkillDto skillDto = findById(id);
    skillRepository.deleteById(skillDto.id());
  }
}