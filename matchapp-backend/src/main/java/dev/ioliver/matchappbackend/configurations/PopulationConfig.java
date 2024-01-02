package dev.ioliver.matchappbackend.configurations;

import dev.ioliver.matchappbackend.dtos.skill.SkillCreateDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaCreateDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.services.SkillAreaService;
import dev.ioliver.matchappbackend.services.SkillService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PopulationConfig {
  private final SkillAreaService skillAreaService;
  private final SkillService skillService;

  @PostConstruct
  public void populate() throws BadRequestException {
    log.info("Populating db...");
    if (skillAreaService.findAll().isEmpty()) {
      skillAreaService.create(
          SkillAreaCreateDto.builder()
              .name("Tecnologia")
              .description("Área de tecnologia")
              .build()
      );
      skillAreaService.create(
          SkillAreaCreateDto.builder()
              .name("Idioma")
              .description("Área de idiomas")
              .build()
      );
    }

    if (skillAreaService.existsByName("Tecnologia")) {
      SkillAreaDto tecnologia = skillAreaService.findByName("Tecnologia");
      if (!skillService.existsByName("Java básico")) {
        skillService.create(
            SkillCreateDto.builder()
                .name("Java básico")
                .description("Curso de java básico")
                .skillAreaId(tecnologia.id())
                .build()
        );
      }
      if (!skillService.existsByName("Java avançado")) {
        skillService.create(
            SkillCreateDto.builder()
                .name("Java avançado")
                .description("Curso de java avançado")
                .skillAreaId(tecnologia.id())
                .build()
        );
      }
      if (!skillService.existsByName("Python avançado")) {
        skillService.create(
            SkillCreateDto.builder()
                .name("Python avançado")
                .description("Curso de python avançado")
                .skillAreaId(tecnologia.id())
                .build()
        );
      }
      if (!skillService.existsByName("C++ básico")) {
        skillService.create(
            SkillCreateDto.builder()
                .name("C++ básico")
                .description("Curso de C++ básico")
                .skillAreaId(tecnologia.id())
                .build()
        );
      }
    }
    if (skillAreaService.existsByName("Idioma")) {
      SkillAreaDto idioma = skillAreaService.findByName("Idioma");
      if (!skillService.existsByName("Português básico")) {
        skillService.create(
            SkillCreateDto.builder()
                .name("Português básico")
                .description("Curso de português básico")
                .skillAreaId(idioma.id())
                .build()
        );
      }
      if (!skillService.existsByName("Inglês básico")) {
        skillService.create(
            SkillCreateDto.builder()
                .name("Inglês básico")
                .description("Curso de inglês básico")
                .skillAreaId(idioma.id())
                .build()
        );
      }
      if (!skillService.existsByName("Espanhol avançado")) {
        skillService.create(
            SkillCreateDto.builder()
                .name("Espanhol avançado")
                .description("Curso de espanhol avançado")
                .skillAreaId(idioma.id())
                .build()
        );
      }
      if (!skillService.existsByName("Japonês básico")) {
        skillService.create(
            SkillCreateDto.builder()
                .name("Japonês básico")
                .description("Curso de japonês básico")
                .skillAreaId(idioma.id())
                .build()
        );
      }
    }
  }
}
