package dev.ioliver.matchappbackend.controllers.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ioliver.matchappbackend.dtos.auth.AuthRequestDto;
import dev.ioliver.matchappbackend.dtos.auth.AuthResponseDto;
import dev.ioliver.matchappbackend.dtos.auth.RefreshRequestDto;
import dev.ioliver.matchappbackend.dtos.user.UserCreateDto;
import dev.ioliver.matchappbackend.mocks.UserMocks;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {
  @Container static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
  @Container static GenericContainer<?> redisContainer =
      new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

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
  void should_RegisterUserSuccessfully_When_ValidUserCreateDto() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(UserMocks.validUserCreateDto())))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

    AuthResponseDto expectedResponse =
        objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

    assertInstanceOf(AuthResponseDto.class, expectedResponse);
    assertNotNull(expectedResponse.accessToken());
    assertNotNull(expectedResponse.refreshToken());
  }

  @Test
  void should_LoginUserSuccessfully_When_ValidCredentials() throws Exception {
    UserCreateDto userCreateDto = UserMocks.validUserCreateDto();
    AuthRequestDto authRequestDto = AuthRequestDto.builder()
        .email(userCreateDto.email())
        .password(userCreateDto.password())
        .build();

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(authRequestDto)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AuthResponseDto expectedResponse =
        objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

    assertInstanceOf(AuthResponseDto.class, expectedResponse);
    assertNotNull(expectedResponse.accessToken());
    assertNotNull(expectedResponse.refreshToken());
  }

  @Test
  void should_generateNewTokens_When_ValidRefreshToken() throws Exception {
    UserCreateDto userCreateDto = UserMocks.validUserCreateDto();
    AuthRequestDto authRequestDto = AuthRequestDto.builder()
        .email(userCreateDto.email())
        .password(userCreateDto.password())
        .build();

    MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(authRequestDto)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AuthResponseDto loginAuthResponseDto =
        objectMapper.readValue(loginResult.getResponse().getContentAsString(),
            new TypeReference<>() {
            });

    RefreshRequestDto refreshRequestDto =
        RefreshRequestDto.builder().refreshToken(loginAuthResponseDto.refreshToken()).build();

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(refreshRequestDto)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AuthResponseDto expectedResponse =
        objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

    assertInstanceOf(AuthResponseDto.class, expectedResponse);
    assertNotNull(expectedResponse.accessToken());
    assertNotNull(expectedResponse.refreshToken());
    assertNotEquals(loginAuthResponseDto.accessToken(), expectedResponse.accessToken());
    assertNotEquals(loginAuthResponseDto.refreshToken(), expectedResponse.refreshToken());
  }

  @Test
  void should_ReturnBadRequest_When_RegistrationRequestDtoIsInvalid() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(UserMocks.userCreateDtoLessThan18())))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void should_ReturnBadRequest_When_LoginRequestDtoIsInvalid() throws Exception {
    AuthRequestDto authRequestDto = AuthRequestDto.builder().email("invalid@email.com").build();

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(authRequestDto)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void should_ReturnBadRequest_When_RefreshRequestDtoIsInvalid() throws Exception {
    RefreshRequestDto refreshRequestDto = RefreshRequestDto.builder().build();

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(refreshRequestDto)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void should_Return401Unauthorized_When_InvalidLoginCredentials() throws Exception {
    AuthRequestDto authRequestDto =
        AuthRequestDto.builder().email("invalid@email.com").password("invalidPassword").build();

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(authRequestDto)))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  void should_Return401Unauthorized_When_RefreshTokenIsInvalid() throws Exception {
    RefreshRequestDto refreshRequestDto =
        RefreshRequestDto.builder().refreshToken("invalidToken").build();

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(refreshRequestDto)))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  void shouldReturn401UnauthorizedWhenRefreshTokenIsNotRefreshToken() throws Exception {
    UserCreateDto userCreateDto = UserMocks.validUserCreateDto();
    AuthRequestDto authRequestDto = AuthRequestDto.builder()
        .email(userCreateDto.email())
        .password(userCreateDto.password())
        .build();

    MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(authRequestDto)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    AuthResponseDto loginAuthResponseDto =
        objectMapper.readValue(loginResult.getResponse().getContentAsString(),
            new TypeReference<>() {
            });

    RefreshRequestDto refreshRequestDto =
        RefreshRequestDto.builder().refreshToken(loginAuthResponseDto.accessToken()).build();

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(refreshRequestDto)))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }
}