package dev.ioliver.matchappbackend.services;

import dev.ioliver.matchappbackend.dtos.user.UserBasicDto;
import dev.ioliver.matchappbackend.dtos.user.UserCreateDto;
import dev.ioliver.matchappbackend.dtos.user.UserDto;
import dev.ioliver.matchappbackend.dtos.userSkill.UserSkillSetDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.mappers.SkillMapper;
import dev.ioliver.matchappbackend.mappers.UserMapper;
import dev.ioliver.matchappbackend.models.User;
import dev.ioliver.matchappbackend.models.UserSkill;
import dev.ioliver.matchappbackend.repositories.UserRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final SkillMapper skillMapper;

  @Transactional(readOnly = true)
  public UserDto findByEmail(String email) throws BadRequestException {
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isEmpty()) throw new BadRequestException("User not found");

    return userMapper.toDto(optionalUser.get());
  }

  @Transactional
  public UserDto create(UserCreateDto dto) throws BadRequestException {
    if (dto.birthDate().until(LocalDate.now(), ChronoUnit.YEARS) < 18)
      throw new BadRequestException("User must be at least 18 years old");

    if (userRepository.existsByEmail(dto.email()))
      throw new BadRequestException("User with this email already exists");

    User user = User.builder()
        .email(dto.email())
        .fullName(dto.fullName().trim())
        .hashedPassword(passwordEncoder.encode(dto.password()))
        .birthDate(dto.birthDate())
        .build();

    User userSaved = userRepository.save(user);

    return userMapper.toDto(userSaved);
  }

  public User findById(Long id) throws BadRequestException {
    return userRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("Dont exist a User with the id: " + id));
  }

  public List<UserBasicDto> findCompatibleUsers(Long id) throws BadRequestException {
    User user = findById(id);
    List<User> compatibleUsers = userRepository.findCompatibleUsers(user);

    return compatibleUsers.stream()
        .map(u -> UserBasicDto.builder()
            .id(u.getId())
            .fullName(u.getFullName())
            .userSkillSet(UserSkillSetDto.builder()
                .learnSkills(u.getSkillsToLearn().stream().map(skillMapper::toDto).toList())
                .teachSkills(u.getSkillsToTeach().stream().map(skillMapper::toDto).toList())
                .build())
            .build())
        .toList();
  }
}
