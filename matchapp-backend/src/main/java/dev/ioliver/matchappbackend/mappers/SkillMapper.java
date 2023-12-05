package dev.ioliver.matchappbackend.mappers;

import dev.ioliver.matchappbackend.dtos.skill.SkillCreateDto;
import dev.ioliver.matchappbackend.dtos.skill.SkillDto;
import dev.ioliver.matchappbackend.dtos.skill.SkillUpdateDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.models.Skill;
import dev.ioliver.matchappbackend.repositories.SkillRepository;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = SkillAreaMapper.class)
public abstract class SkillMapper {

  @Autowired private SkillRepository skillRepository;

  public SkillDto toDto(Long id) throws BadRequestException {
    return toDto(toEntity(id));
  }

  public List<Skill> toListEntity(List<Long> ids) throws BadRequestException {
    List<Skill> list = new ArrayList<>();
    for (Long id : ids) {
      Skill entity = toEntity(id);
      list.add(entity);
    }
    return list;
  }

  public Skill toEntity(Long id) throws BadRequestException {
    return skillRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("Don't exist a Skill with the id: " + id));
  }

  public abstract SkillDto toDto(Skill skill);

  public abstract Skill toEntity(SkillDto skillDto);

  @Mapping(source = "skillAreaId", target = "skillArea")
  public abstract Skill createToEntity(SkillCreateDto skillCreateDto);

  @Mapping(source = "skillAreaId", target = "skillArea")
  public abstract void updateToEntity(SkillUpdateDto skillUpdateDto, @MappingTarget Skill skill);

  public abstract List<SkillDto> toListDto(List<Skill> skills);
}
