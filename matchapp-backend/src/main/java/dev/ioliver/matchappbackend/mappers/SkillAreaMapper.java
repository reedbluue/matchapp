package dev.ioliver.matchappbackend.mappers;

import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaCreateDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaUpdateDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.models.SkillArea;
import dev.ioliver.matchappbackend.repositories.SkillAreaRepository;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class SkillAreaMapper {

  @Autowired private SkillAreaRepository skillAreaRepository;

  public SkillArea toEntity(Long id) throws BadRequestException {
    return skillAreaRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("Don't exist a SkillArea with the id: " + id));
  }

  public SkillAreaDto toDto(Long id) throws BadRequestException {
    return toDto(toEntity(id));
  }


  public abstract SkillAreaDto toDto(SkillArea skillArea);

  public abstract SkillArea toEntity(SkillAreaDto skillAreaDto);

  public abstract SkillArea createToEntity(SkillAreaCreateDto skillAreaCreateDto);

  public abstract void updateToEntity(SkillAreaUpdateDto skillAreaUpdateDto,
      @MappingTarget SkillArea skillArea);

  public abstract List<SkillAreaDto> toListDto(List<SkillArea> skillAreas);
}
