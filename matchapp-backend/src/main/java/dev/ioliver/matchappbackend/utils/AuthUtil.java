package dev.ioliver.matchappbackend.utils;

import dev.ioliver.matchappbackend.models.security.UserDetailsImp;
import java.security.Principal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public abstract class AuthUtil {
  public static UserDetailsImp toUserDetailsImp(UsernamePasswordAuthenticationToken principal) {
    return (UserDetailsImp) principal.getPrincipal();
  }
}
