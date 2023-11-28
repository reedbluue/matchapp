package dev.ioliver.matchappbackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.exceptions.UnauthorizedException;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {
  private static final String CLAIM_REFRESH_TOKEN = "isRefreshToken";
  @Value("${jwt.secret}") private String secret;
  @Value("${jwt.accessTokenExpiry}") private int accessTokenExpiry;
  @Value("${jwt.refreshTokenExpiry}") private int refreshTokenExpiry;

  private Algorithm algorithm;
  private JWTVerifier verifier;

  @PostConstruct
  public void init() {
    algorithm = Algorithm.HMAC512(secret);
    verifier = JWT.require(algorithm).build();
  }

  public String generateAccessToken(@NonNull UserDto user) {
    return JWT.create()
        .withSubject(user.email())
        .withExpiresAt(Instant.now().plus(accessTokenExpiry, ChronoUnit.MINUTES))
        .withClaim(CLAIM_REFRESH_TOKEN, false)
        .withJWTId(UUID.randomUUID().toString())
        .sign(algorithm);
  }

  public String generateRefreshToken(@NonNull UserDto user) {
    return JWT.create()
        .withSubject(user.email())
        .withExpiresAt(Instant.now().plus(refreshTokenExpiry, ChronoUnit.MINUTES))
        .withClaim(CLAIM_REFRESH_TOKEN, true)
        .withJWTId(UUID.randomUUID().toString())
        .sign(algorithm);
  }

  public TokenDataDto validateToken(@NonNull String token) throws UnauthorizedException {
    try {
      DecodedJWT decodedJWT = verifier.verify(token);
      return TokenDataDto.builder()
          .token(token)
          .email(decodedJWT.getSubject())
          .isRefreshToken(decodedJWT.getClaim(CLAIM_REFRESH_TOKEN).asBoolean())
          .build();
    } catch (Exception e) {
      log.warn("Invalid token: {}, cause: {}", token, e.getMessage());
      throw new UnauthorizedException();
    }
  }

  @Builder
  public record TokenDataDto(String token, String email, boolean isRefreshToken) {
  }
}
