package dev.ioliver.matchappbackend.mappers;

import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.models.User;
import dev.ioliver.matchappbackend.repositories.UserRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

  @Autowired UserRepository userRepository;

  public UserDto toDto(Long id) throws BadRequestException {
    return toDto(toEntity(id));
  }

  public User toEntity(Long id) throws BadRequestException {
    return userRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("Don't exist a User with the id: " + id));
  }

  public abstract UserDto toDto(User user);

  public abstract User toEntity(UserDto userDto);
}
