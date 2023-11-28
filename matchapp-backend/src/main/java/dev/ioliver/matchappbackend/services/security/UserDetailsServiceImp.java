package dev.ioliver.matchappbackend.services.security;

import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.models.security.UserDetailsImp;
import dev.ioliver.matchappbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
  private final UserService userService;

  @Override
  public UserDetailsImp loadUserByUsername(String email) throws UsernameNotFoundException {
    try {
      UserDto user = userService.findByEmail(email);
      return new UserDetailsImp(user);
    } catch (Exception e) {
      throw new UsernameNotFoundException("User not found");
    }
  }
}
