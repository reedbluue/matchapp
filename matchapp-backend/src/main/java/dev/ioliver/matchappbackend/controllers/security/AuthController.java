package dev.ioliver.matchappbackend.controllers.security;

import dev.ioliver.matchappbackend.dtos.auth.AuthInfoDto;
import dev.ioliver.matchappbackend.dtos.auth.AuthRequestDto;
import dev.ioliver.matchappbackend.dtos.auth.AuthResponseDto;
import dev.ioliver.matchappbackend.dtos.auth.RefreshRequestDto;
import dev.ioliver.matchappbackend.dtos.user.UserCreateDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.exceptions.UnauthorizedException;
import dev.ioliver.matchappbackend.services.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth Controller")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
  @Operation(description = "This endpoint is used to login a user", summary = "Login of a user")
  public AuthResponseDto login(@RequestBody @Valid AuthRequestDto dto)
      throws UnauthorizedException {
    return authService.login(dto);
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  @Operation(description = "This endpoint is used to register a user",
      summary = "Registration of a user")
  public AuthResponseDto login(@RequestBody @Valid UserCreateDto dto) throws BadRequestException {
    return authService.registration(dto);
  }

  @PostMapping("/refresh")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponse(responseCode = "401", description = "Bad Request", content = @Content())
  @Operation(description = "This endpoint is used to refresh a token",
      summary = "Refresh of a token")
  public AuthResponseDto refresh(@RequestBody @Valid RefreshRequestDto dto)
      throws UnauthorizedException {
    return authService.refresh(dto);
  }

  @GetMapping("/info")
  @ResponseStatus(HttpStatus.OK)
  @SecurityRequirement(name = "Bearer")
  @ApiResponse(responseCode = "401", description = "Bad Request", content = @Content())
  @Operation(description = "This endpoint is used to return user auth info",
      summary = "Return user auth info")
  public AuthInfoDto me(UsernamePasswordAuthenticationToken authToken) {
    return authService.info(authToken);
  }
}
