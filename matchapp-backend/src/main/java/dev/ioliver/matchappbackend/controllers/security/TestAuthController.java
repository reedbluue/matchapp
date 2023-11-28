package dev.ioliver.matchappbackend.controllers.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test-auth")
@SecurityRequirement(name = "Bearer")
@Tag(name = "Test Controller")
public class TestAuthController {

  @GetMapping("/authenticated")
  @Operation(summary = "Only for authenticated users",
      description = "This endpoint is only for authenticated users")
  public String authenticated() {
    return "Authenticated";
  }

  @GetMapping("/user")
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @Operation(summary = "Only for users", description = "This endpoint is only for users")
  public String isUser() {
    return "You are an user";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Only for admins", description = "This endpoint is only for admins")
  public String isAdmin(Principal principal) {
    return "You are an admin";
  }
}
