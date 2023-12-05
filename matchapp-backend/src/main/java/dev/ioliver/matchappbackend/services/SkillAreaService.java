package dev.ioliver.matchappbackend.services;

import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaCreateDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaUpdateDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.mappers.SkillAreaMapper;
import dev.ioliver.matchappbackend.models.SkillArea;
import dev.ioliver.matchappbackend.repositories.SkillAreaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SkillAreaService {
  private final SkillAreaRepository skillAreaRepository;
  private final SkillAreaMapper skillAreaMapper;

  @Transactional(readOnly = true)
  public List<SkillAreaDto> findAll() {
    List<SkillArea> all = skillAreaRepository.findAll();
    return skillAreaMapper.toListDto(all);
  }

  @Transactional(readOnly = true)
  public SkillAreaDto findById(Long id) throws BadRequestException {
    SkillArea skillArea = skillAreaRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("Don't exist a SkillArea with the id: " + id));
    return skillAreaMapper.toDto(skillArea);
  }

  @Transactional
  public SkillAreaDto create(SkillAreaCreateDto dto) throws BadRequestException {
    if (skillAreaRepository.existsByName(dto.name()))
      throw new BadRequestException("Already exists a SkillArea with the name: " + dto.name());

    SkillArea entity = skillAreaMapper.createToEntity(dto);

    SkillArea saved = skillAreaRepository.save(entity);
    return skillAreaMapper.toDto(saved);
  }

  @Transactional
  public SkillAreaDto update(SkillAreaUpdateDto dto) throws BadRequestException {
    SkillAreaDto skillAreaDto = findById(dto.id());
    SkillArea entity = skillAreaMapper.toEntity(skillAreaDto);
    skillAreaMapper.updateToEntity(dto, entity);

    SkillArea saved = skillAreaRepository.save(entity);
    return skillAreaMapper.toDto(saved);
  }

  @Transactional
  public void deleteById(Long id) throws BadRequestException {
    SkillAreaDto skillAreaDto = findById(id);
    skillAreaRepository.deleteById(skillAreaDto.id());
  }
}