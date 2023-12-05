package dev.ioliver.matchappbackend.services;

import dev.ioliver.matchappbackend.dtos.skill.SkillCreateDto;
import dev.ioliver.matchappbackend.dtos.skill.SkillDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.mocks.SkillAreaMocks;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SkillServiceTest {
  @Container static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

  @Container static GenericContainer<?> redisContainer =
      new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

  @Autowired SkillService skillService;
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
  void test() throws BadRequestException {
    SkillAreaDto skillAreaDto = skillAreaService.create(SkillAreaMocks.validSkillAreaCreateDto());

    SkillCreateDto skillCreateDto = SkillCreateDto.builder()
        .name("test")
        .description("test")
        .skillAreaId(skillAreaDto.id())
        .build();
    SkillDto skillDto = skillService.create(skillCreateDto);
    log.info("{}", skillDto);
  }
}