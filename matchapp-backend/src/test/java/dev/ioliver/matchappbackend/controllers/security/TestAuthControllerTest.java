package dev.ioliver.matchappbackend.controllers.security;

import dev.ioliver.matchappbackend.enums.Role;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.mappers.UserMapper;
import dev.ioliver.matchappbackend.models.User;
import dev.ioliver.matchappbackend.repositories.UserRepository;
import dev.ioliver.matchappbackend.utils.JwtUtil;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class TestAuthControllerTest {
  @Container private final static PostgreSQLContainer<?> postgreSQLContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
  @Container private final static GenericContainer<?> redisContainer =
      new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

  private static String accessTokenAuth;
  private static String accessTokenUser;
  private static String accessTokenAdmin;
  private static String refreshTokenAdmin;
  private static boolean usersCreated = false;

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private JwtUtil jwtUtil;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private UserMapper userMapper;

  @DynamicPropertySource
  private static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);

    registry.add("spring.redis.host", redisContainer::getHost);
    registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
  }

  @BeforeEach
  void createUsers() throws BadRequestException {
    if (usersCreated) return;

    User auth = userRepository.save(User.builder()
        .email("auth@auth.com")
        .fullName("Auth Auth")
        .hashedPassword(passwordEncoder.encode("authauth"))
        .birthDate(LocalDate.now().minusYears(18))
        .roles(List.of())
        .build());

    User user = userRepository.save(User.builder()
        .email("user@user.com")
        .fullName("User User")
        .hashedPassword(passwordEncoder.encode("useruser"))
        .birthDate(LocalDate.now().minusYears(18))
        .roles(List.of(Role.ROLE_USER))
        .build());

    User admin = userRepository.save(User.builder()
        .email("admin@admin.com")
        .fullName("Admin Admin")
        .hashedPassword(passwordEncoder.encode("adminadmin"))
        .birthDate(LocalDate.now().minusYears(18))
        .roles(List.of(Role.ROLE_ADMIN, Role.ROLE_USER))
        .build());

    accessTokenAuth = jwtUtil.generateAccessToken(userMapper.toDto(auth));
    accessTokenUser = jwtUtil.generateAccessToken(userMapper.toDto(user));
    accessTokenAdmin = jwtUtil.generateAccessToken(userMapper.toDto(admin));
    refreshTokenAdmin = jwtUtil.generateRefreshToken(userMapper.toDto(admin));
    usersCreated = true;
  }

  @Test
  void should_Return200_When_AuthAccessAuthEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/test-auth/authenticated")
            .header("Authorization", "Bearer " + accessTokenAuth))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
  }

  @Test
  void should_Return200_When_UserAccessUserEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/test-auth/user")
            .header("Authorization", "Bearer " + accessTokenUser))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
  }

  @Test
  void should_Return200_When_AdminAccessAdminEndpoint() throws Exception {
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/test-auth/admin")
            .header("Authorization", "Bearer " + accessTokenAdmin))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    System.out.println();
  }

  @Test
  void should_Return200_When_AdminAccessUserEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/test-auth/user")
            .header("Authorization", "Bearer " + accessTokenAdmin))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
  }

  @Test
  void should_Return403_When_AuthAccessUserEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/test-auth/user")
            .header("Authorization", "Bearer " + accessTokenAuth))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andReturn();
  }

  @Test
  void should_Return403_When_UserAccessAdminEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/test-auth/admin")
            .header("Authorization", "Bearer " + accessTokenUser))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andReturn();
  }

  @Test
  void should_Return401_When_TokenIsNotPresent() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/test-auth/admin"))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andReturn();
  }

  @Test
  void should_Return401_When_TokenIsInvalid() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/test-auth/admin")
            .header("Authorization", "Bearer invalidToken"))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andReturn();
  }

  @Test
  void should_Return401_When_TokenIsNotAccessToken() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/test-auth/admin")
            .header("Authorization", "Bearer " + refreshTokenAdmin))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andReturn();
  }
}