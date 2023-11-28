package dev.ioliver.matchappbackend.models.security;

import dev.ioliver.matchappbackend.dtos.user.UserDto;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class UserDetailsImp implements UserDetails {
  private final UserDto user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.roles().stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
  }

  @Override
  public String getPassword() {
    return user.hashedPassword();
  }

  @Override
  public String getUsername() {
    return user.email();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.active();
  }
}
