package dev.ioliver.matchappbackend.services.security;

import dev.ioliver.matchappbackend.dtos.auth.AuthRequestDto;
import dev.ioliver.matchappbackend.dtos.auth.AuthResponseDto;
import dev.ioliver.matchappbackend.dtos.auth.RefreshRequestDto;
import dev.ioliver.matchappbackend.dtos.user.UserCreateDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.exceptions.UnauthorizedException;
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

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest {
  @Container static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

  @Container static GenericContainer<?> redisContainer =
      new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

  @Autowired private AuthService authService;

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
  void should_ReturnAccessAndRefreshTokens_When_RegistrationWithValidCredentials()
      throws BadRequestException {
    UserCreateDto userCreateDto = UserMocks.validUserCreateDto();
    AuthResponseDto response = authService.registration(userCreateDto);

    assertInstanceOf(AuthResponseDto.class, response);
    assertNotNull(response.accessToken());
    assertNotNull(response.refreshToken());
  }

  @Test
  void should_ReturnAccessAndRefreshTokens_When_LoginWithValidCredentials()
      throws UnauthorizedException {
    UserCreateDto userCreateDto = UserMocks.validUserCreateDto();

    AuthRequestDto request = AuthRequestDto.builder()
        .email(userCreateDto.email())
        .password(userCreateDto.password())
        .build();

    AuthResponseDto response = authService.login(request);

    assertInstanceOf(AuthResponseDto.class, response);
    assertNotNull(response.accessToken());
    assertNotNull(response.refreshToken());
  }

  @Test
  void should_ReturnNewAccessAndRefreshTokens_When_RefreshWithValidRefreshToken()
      throws UnauthorizedException {
    UserCreateDto userCreateDto = UserMocks.validUserCreateDto();

    AuthRequestDto request = AuthRequestDto.builder()
        .email(userCreateDto.email())
        .password(userCreateDto.password())
        .build();

    AuthResponseDto tokens = authService.login(request);
    RefreshRequestDto dto = RefreshRequestDto.builder().refreshToken(tokens.refreshToken()).build();

    AuthResponseDto response = authService.refresh(dto);

    assertInstanceOf(AuthResponseDto.class, response);
    assertNotNull(response.accessToken());
    assertNotNull(response.refreshToken());
  }

  @Test
  void should_ThrowUnauthorizedException_When_loginWithInvalidCredentials() {
    AuthRequestDto request =
        AuthRequestDto.builder().email("invalid@email.com").password("invalidPassword").build();

    assertThrows(UnauthorizedException.class, () -> authService.login(request));
  }

  @Test
  void should_ThrowUnauthorizedException_When_refreshWithInvalidRefreshToken()
      throws UnauthorizedException {
    UserCreateDto userCreateDto = UserMocks.validUserCreateDto();

    AuthRequestDto request = AuthRequestDto.builder()
        .email(userCreateDto.email())
        .password(userCreateDto.password())
        .build();

    AuthResponseDto tokens = authService.login(request);

    RefreshRequestDto refreshRequestDto =
        RefreshRequestDto.builder().refreshToken(tokens.accessToken()).build();

    assertThrows(UnauthorizedException.class, () -> authService.refresh(refreshRequestDto));
  }

  @Test
  void should_ThrowUnauthorizedException_When_refreshWithOldRefreshToken()
      throws UnauthorizedException {
    UserCreateDto userCreateDto = UserMocks.validUserCreateDto();

    AuthRequestDto request = AuthRequestDto.builder()
        .email(userCreateDto.email())
        .password(userCreateDto.password())
        .build();

    AuthResponseDto tokens = authService.login(request);

    authService.login(request);

    RefreshRequestDto refreshRequestDto =
        RefreshRequestDto.builder().refreshToken(tokens.refreshToken()).build();

    assertThrows(UnauthorizedException.class, () -> authService.refresh(refreshRequestDto));
  }
}