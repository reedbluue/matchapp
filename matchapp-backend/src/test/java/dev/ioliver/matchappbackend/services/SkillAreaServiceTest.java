package dev.ioliver.matchappbackend.services;

import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaCreateDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaUpdateDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.mocks.SkillAreaMocks;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SkillAreaServiceTest {
  @Container static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

  @Container static GenericContainer<?> redisContainer =
      new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

  @Autowired SkillAreaService skillAreaService;

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);

    registry.add("spring.redis.host", redisContainer::getHost);
    registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
  }

  @Test
  @Order(1)
  void should_SaveNewSkillArea_When_ValidCreateDto() throws BadRequestException {
    SkillAreaCreateDto skillAreaCreateDto = SkillAreaMocks.validSkillAreaCreateDto();

    SkillAreaDto skillAreaDto = skillAreaService.create(skillAreaCreateDto);

    assertNotNull(skillAreaDto);
    assertNotNull(skillAreaDto.id());
    assertEquals(skillAreaCreateDto.name(), skillAreaDto.name());
    assertEquals(skillAreaCreateDto.description(), skillAreaDto.description());
  }

  @Test
  void should_ReturnListOfSkillAreaDto_When_FindAll() throws BadRequestException {
    SkillAreaDto skillAreaDto = skillAreaService.create(SkillAreaMocks.validSkillAreaCreateDto());

    List<SkillAreaDto> result = skillAreaService.findAll();

    assertEquals(2, result.size());
  }

  @Test
  void should_ReturnsSkillAreaDtoWithGivenId_When_findById() throws BadRequestException {
    SkillAreaCreateDto skillAreaCreateDto = SkillAreaMocks.validSkillAreaCreateDto();
    SkillAreaDto skillAreaDto = skillAreaService.create(skillAreaCreateDto);

    SkillAreaDto result = skillAreaService.findById(skillAreaDto.id());

    assertEquals(skillAreaDto.id(), result.id());
  }

  @Test
  void should_updateAndReturnsSkillAreaDto_When_update() throws BadRequestException {
    SkillAreaCreateDto skillAreaCreateDto = SkillAreaMocks.validSkillAreaCreateDto();
    SkillAreaDto skillAreaDto = skillAreaService.create(skillAreaCreateDto);

    SkillAreaUpdateDto skillAreaUpdateDto =
        SkillAreaMocks.validSkillAreaUpdateDto(skillAreaDto.id());

    SkillAreaDto result = skillAreaService.update(skillAreaUpdateDto);

    assertEquals(skillAreaUpdateDto.id(), result.id());
    assertEquals(skillAreaUpdateDto.name(), result.name());
    assertEquals(skillAreaUpdateDto.description(), result.description());
  }

  @Test
  void should_DeleteSkillArea_When_ValidIdIsProvided() throws BadRequestException {
    SkillAreaCreateDto skillAreaCreateDto = SkillAreaMocks.validSkillAreaCreateDto();
    SkillAreaDto skillAreaDto = skillAreaService.create(skillAreaCreateDto);

    skillAreaService.deleteById(skillAreaDto.id());

    assertThrows(BadRequestException.class, () -> skillAreaService.findById(skillAreaDto.id()));
  }

  @Test
  void should_ThrowsBadRequestException_When_IdNotFound() {
    assertThrows(BadRequestException.class, () -> {
      skillAreaService.findById(9999999L);
    });
  }

  @Test
  void should_ThrowBadRequestException_When_SkillAreaWithSameNameExists()
      throws BadRequestException {
    SkillAreaCreateDto skillAreaCreateDto = SkillAreaMocks.validSkillAreaCreateDto();
    skillAreaService.create(skillAreaCreateDto);

    assertThrows(BadRequestException.class, () -> skillAreaService.create(skillAreaCreateDto));
  }

  @Test
  void should_ThrowsBadRequestException_When_updateWithInvalidId() throws BadRequestException {
    assertThrows(BadRequestException.class,
        () -> skillAreaService.update(SkillAreaMocks.validSkillAreaUpdateDto(9999999L)));
  }

  @Test
  void should_ThrowBadRequestException_When_IdNotFound() {
    assertThrows(BadRequestException.class, () -> skillAreaService.deleteById(9999999L));
  }
}