package dev.ioliver.matchappbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaDto;
import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.mocks.SkillAreaMocks;
import dev.ioliver.matchappbackend.mocks.UserMocks;
import dev.ioliver.matchappbackend.services.UserService;
import dev.ioliver.matchappbackend.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
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
class SkillAreaControllerTest {
  @Container static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
  @Container static GenericContainer<?> redisContainer =
      new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
  static private String token;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private UserService userService;
  @Autowired private JwtUtil jwtUtil;

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);

    registry.add("spring.redis.host", redisContainer::getHost);
    registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
  }

  @BeforeEach
  void beforeAll() throws BadRequestException {
    UserDto userDto = userService.create(UserMocks.validUserCreateDto());
    token = jwtUtil.generateAccessToken(userDto);
  }

  @Test
  void should_201_When_ValidSkillAreaCreateDto() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/skill-area")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(SkillAreaMocks.validSkillAreaCreateDto()))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn();

    SkillAreaDto skillAreaDto =
        objectMapper.readValue(result.getResponse().getContentAsString(), SkillAreaDto.class);

    assertNotNull(skillAreaDto);
    assertInstanceOf(SkillAreaDto.class, skillAreaDto);
    assertNotEquals(0, skillAreaDto.id());
  }
}