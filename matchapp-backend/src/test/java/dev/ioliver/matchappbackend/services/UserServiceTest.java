package dev.ioliver.matchappbackend.services;

import dev.ioliver.matchappbackend.dtos.user.UserCreateDto;
import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.enums.Role;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.mocks.UserMocks;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
  @Container static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

  @Container static GenericContainer<?> redisContainer =
      new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

  @Autowired private UserService userService;

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
  void should_CreateNewUser_When_ValidUserCreateDtoProvided() throws BadRequestException {
    UserCreateDto userCreateDto = UserMocks.validUserCreateDto();

    UserDto result = userService.create(userCreateDto);

    assertNotNull(result);
    assertInstanceOf(UserDto.class, result);
    assertNotNull(result.id());
    assertNotNull(result.fullName());
    assertEquals(userCreateDto.email(), result.email());
    assertEquals(userCreateDto.birthDate(), result.birthDate());
    assertTrue(result.active());
    assertFalse(result.emailVerified());
    assertNotNull(result.createdAt());
    assertTrue(result.roles().contains(Role.ROLE_USER));
  }

  @Test
  void should_FindUserByEmail_When_UserExists() throws BadRequestException {
    UserCreateDto userCreateDto = UserMocks.validUserCreateDto();

    UserDto result = userService.findByEmail(userCreateDto.email());

    assertNotNull(result);
    assertInstanceOf(UserDto.class, result);
  }

  @Test
  void should_ThrowBadRequestException_When_UserNotFoundByEmail() {
    assertThrows(BadRequestException.class, () -> userService.findByEmail("invalid@email.com"));
  }

  @Test
  void should_ThrowBadRequestException_When_UserIsLessThan18YearsOld() {
    UserCreateDto dto = UserMocks.userCreateDtoLessThan18();
    assertThrows(BadRequestException.class, () -> userService.create(dto));
  }

  @Test
  void should_ThrowBadRequestException_When_UserHasShortPassword() {
    UserCreateDto dto = UserMocks.userCreateDtoShortPassword();
    assertThrows(BadRequestException.class, () -> userService.create(dto));
  }

  @Test
  void should_ThrowBadRequestException_When_UserWithEmailAlreadyExists() {
    UserCreateDto dto = UserMocks.validUserCreateDto();
    assertThrows(BadRequestException.class, () -> userService.create(dto));
  }
}