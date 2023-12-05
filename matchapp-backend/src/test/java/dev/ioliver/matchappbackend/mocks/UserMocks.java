package dev.ioliver.matchappbackend.mocks;

import dev.ioliver.matchappbackend.dtos.user.UserCreateDto;
import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.enums.Role;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public abstract class UserMocks {
  public static UserDto validUserDto() {
    return UserDto.builder()
        .id((long) (Math.random() * 1000))
        .email("test@example.com")
        .hashedPassword("hashedPassword")
        .username("username")
        .emailVerified(true)
        .active(true)
        .birthDate(LocalDate.now().minusYears(18))
        .createdAt(Instant.now())
        .roles(List.of(Role.ROLE_USER))
        .build();
  }

  public static UserCreateDto validUserCreateDto() {
    return UserCreateDto.builder()
        .email("test@example.com")
        .password("password")
        .birthDate(LocalDate.now().minusYears(18))
        .build();
  }

  public static UserCreateDto userCreateDtoLessThan18() {
    return UserCreateDto.builder()
        .email("test@example.com")
        .password("password")
        .birthDate(LocalDate.now().minusYears(17))
        .build();
  }

  public static UserCreateDto userCreateDtoShortPassword() {
    return UserCreateDto.builder()
        .email("test@example.com")
        .password("pass")
        .birthDate(LocalDate.now().minusYears(18))
        .build();
  }
}
