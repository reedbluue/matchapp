package dev.ioliver.matchappbackend.mocks;

import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaCreateDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaUpdateDto;
import java.util.UUID;

public abstract class SkillAreaMocks {
  public static SkillAreaDto validSkillAreaDto() {
    return SkillAreaDto.builder()
        .id((long) (Math.random() * 1000))
        .name("Skill Area Name")
        .description("Skill Area Description")
        .build();
  }

  public static SkillAreaCreateDto validSkillAreaCreateDto() {
    return SkillAreaCreateDto.builder()
        .name("Skill Area Name - " + UUID.randomUUID())
        .description("Skill Area Description - " + UUID.randomUUID())
        .build();
  }

  public static SkillAreaUpdateDto validSkillAreaUpdateDto(Long id) {
    return SkillAreaUpdateDto.builder()
        .id(id)
        .name("Skill Area Name - " + UUID.randomUUID())
        .description("Skill Area Description - " + UUID.randomUUID())
        .build();
  }
}
