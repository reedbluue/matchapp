package dev.ioliver.matchappbackend.controllers;

import dev.ioliver.matchappbackend.dtos.user.UserInfoDto;
import dev.ioliver.matchappbackend.services.UserService;
import dev.ioliver.matchappbackend.services.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User Controller")
@SecurityRequirement(name = "Bearer")
public class UserController {
  private final UserService userService;
  private final AuthService authService;

  @GetMapping("/info")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
  @Operation(description = "This endpoint is used to return user info",
      summary = "Return user info")
  public UserInfoDto info(UsernamePasswordAuthenticationToken authToken) {
    return authService.userInfo(authToken);
  }
}
