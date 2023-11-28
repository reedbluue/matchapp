package dev.ioliver.matchappbackend.services.security;

import dev.ioliver.matchappbackend.daos.RedisDao;
import dev.ioliver.matchappbackend.dtos.auth.AuthInfoDto;
import dev.ioliver.matchappbackend.dtos.auth.AuthRequestDto;
import dev.ioliver.matchappbackend.dtos.auth.AuthResponseDto;
import dev.ioliver.matchappbackend.dtos.auth.RefreshRequestDto;
import dev.ioliver.matchappbackend.dtos.user.UserCreateDto;
import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.exceptions.UnauthorizedException;
import dev.ioliver.matchappbackend.models.security.UserDetailsImp;
import dev.ioliver.matchappbackend.services.UserService;
import dev.ioliver.matchappbackend.utils.JwtUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final UserService userService;
  private final RedisDao redisDao;

  @Transactional
  public AuthResponseDto login(AuthRequestDto dto) throws UnauthorizedException {
    Authentication auth;

    try {
      auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
    } catch (Exception e) {
      throw new UnauthorizedException();
    }

    UserDetailsImp userDetailsImp = (UserDetailsImp) auth.getPrincipal();

    String accessToken = jwtUtil.generateAccessToken(userDetailsImp.getUser());
    String refreshToken = jwtUtil.generateRefreshToken(userDetailsImp.getUser());

    redisDao.set(userDetailsImp.getUser().email(), refreshToken);

    return AuthResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
  }

  @Transactional
  public AuthResponseDto registration(UserCreateDto dto) throws BadRequestException {
    UserDto userDto = userService.create(dto);
    UserDetailsImp userDetailsImp = new UserDetailsImp(userDto);

    String accessToken = jwtUtil.generateAccessToken(userDetailsImp.getUser());
    String refreshToken = jwtUtil.generateRefreshToken(userDetailsImp.getUser());

    redisDao.set(userDetailsImp.getUser().email(), refreshToken);

    return AuthResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
  }

  @Transactional
  public AuthResponseDto refresh(RefreshRequestDto dto) throws UnauthorizedException {
    JwtUtil.TokenDataDto tokenDataDto = jwtUtil.validateToken(dto.refreshToken());

    if (!tokenDataDto.isRefreshToken()) throw new UnauthorizedException();

    Optional<String> savedToken = redisDao.get(tokenDataDto.email());

    if (savedToken.isEmpty() || !savedToken.get().equals(dto.refreshToken()))
      throw new UnauthorizedException();

    UserDto userDto;

    try {
      userDto = userService.findByEmail(tokenDataDto.email());
    } catch (BadRequestException e) {
      throw new UnauthorizedException();
    }

    String accessToken = jwtUtil.generateAccessToken(userDto);
    String refreshToken = jwtUtil.generateRefreshToken(userDto);

    redisDao.set(userDto.email(), refreshToken);

    return AuthResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
  }

  @Transactional
  public AuthInfoDto info(UsernamePasswordAuthenticationToken authToken) {
    UserDetailsImp userDetailsImp = (UserDetailsImp) authToken.getPrincipal();

    return AuthInfoDto.builder()
        .email(userDetailsImp.getUser().email())
        .username(userDetailsImp.getUser().username())
        .roles(userDetailsImp.getUser().roles())
        .build();
  }
}
