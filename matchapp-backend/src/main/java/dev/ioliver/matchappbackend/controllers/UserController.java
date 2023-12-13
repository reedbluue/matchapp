package dev.ioliver.matchappbackend.controllers;

import dev.ioliver.matchappbackend.dtos.user.UserBasicDto;
import dev.ioliver.matchappbackend.dtos.user.UserInfoDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.models.security.UserDetailsImp;
import dev.ioliver.matchappbackend.services.UserService;
import dev.ioliver.matchappbackend.services.security.AuthService;
import dev.ioliver.matchappbackend.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

  @GetMapping("/compatible")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
  @Operation(description = "This endpoint is used to return compatible users",
      summary = "Return compatible users")
  public List<UserBasicDto> compatible(UsernamePasswordAuthenticationToken token)
      throws BadRequestException {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(token);
    return userService.findCompatibleUsers(userDetailsImp.getUser().id());
  }
}
