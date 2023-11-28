package dev.ioliver.matchappbackend.utils;

import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.exceptions.UnauthorizedException;
import dev.ioliver.matchappbackend.mocks.UserMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JwtUtilTest {
  @Autowired JwtUtil jwtUtil;

  @Test
  void should_GenerateAccessToken_When_ValidUser() {
    String refreshToken = jwtUtil.generateAccessToken(UserMocks.validUserDto());
    assertNotNull(refreshToken);
  }

  @Test
  void should_GenerateRefreshToken_When_ValidUser() {
    String refreshToken = jwtUtil.generateRefreshToken(UserMocks.validUserDto());
    assertNotNull(refreshToken);
  }

  @Test
  void should_JwtDifferent_When_Created() {
    String accessToken = jwtUtil.generateRefreshToken(UserMocks.validUserDto());
    String accessToken2 = jwtUtil.generateRefreshToken(UserMocks.validUserDto());

    String refreshToken = jwtUtil.generateRefreshToken(UserMocks.validUserDto());
    String refreshToken2 = jwtUtil.generateRefreshToken(UserMocks.validUserDto());

    assertNotEquals(accessToken, accessToken2);
    assertNotEquals(refreshToken, refreshToken2);
  }

  @Test
  void should_GenerateTokenData_When_ValidAccessToken() throws UnauthorizedException {
    String refreshToken = jwtUtil.generateAccessToken(UserMocks.validUserDto());
    JwtUtil.TokenDataDto tokenData = jwtUtil.validateToken(refreshToken);

    UserDto userDto = UserMocks.validUserDto();

    assertNotNull(tokenData);
    assertEquals(refreshToken, tokenData.token());
    assertEquals(userDto.email(), tokenData.email());
    assertFalse(tokenData.isRefreshToken());
  }

  @Test
  void should_GenerateTokenData_When_ValidRefreshToken() throws UnauthorizedException {
    String refreshToken = jwtUtil.generateRefreshToken(UserMocks.validUserDto());
    JwtUtil.TokenDataDto tokenData = jwtUtil.validateToken(refreshToken);

    UserDto userDto = UserMocks.validUserDto();

    assertNotNull(tokenData);
    assertEquals(refreshToken, tokenData.token());
    assertEquals(userDto.email(), tokenData.email());
    assertTrue(tokenData.isRefreshToken());
  }

  @Test
  void should_ThrowException_When_NullUserInput() {
    assertThrows(NullPointerException.class, () -> jwtUtil.generateAccessToken(null));
    assertThrows(NullPointerException.class, () -> jwtUtil.generateRefreshToken(null));
  }

  @Test
  void should_ThrowException_When_NullTokenInput() {
    assertThrows(NullPointerException.class, () -> jwtUtil.validateToken(null));
  }

  @Test
  void should_ThrowException_When_InvalidTokenSignature() {
    String invalidToken = "invalid_token";
    assertThrows(UnauthorizedException.class, () -> jwtUtil.validateToken(invalidToken));
  }
}