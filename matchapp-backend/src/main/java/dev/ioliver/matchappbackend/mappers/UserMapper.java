package dev.ioliver.matchappbackend.mappers;

import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
  UserMapper instance = Mappers.getMapper(UserMapper.class);

  UserDto toDto(User user);

  User toEntity(UserDto userDto);
}
