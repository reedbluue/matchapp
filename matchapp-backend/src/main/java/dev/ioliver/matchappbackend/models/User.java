package dev.ioliver.matchappbackend.models;

import dev.ioliver.matchappbackend.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, updatable = false) private String email;

  @Column(nullable = false) private String hashedPassword;

  @Builder.Default
  @Column(nullable = false, unique = true)
  private String username = "User@".concat(UUID.randomUUID().toString());

  @Builder.Default private boolean emailVerified = false;

  @Builder.Default private boolean active = true;

  @Column(nullable = false) private LocalDate birthDate;

  @Builder.Default private Instant createdAt = Instant.now();

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private List<Role> roles = List.of(Role.ROLE_USER);

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) private List<UserSkill> skillsToLearn;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) private List<UserSkill> skillsToTeach;

  public List<Skill> getSkillsToTeach() {
    return skillsToLearn.stream()
        .filter(UserSkill::getIsTeachingSkill)
        .map(UserSkill::getSkill)
        .toList();
  }

  public List<Skill> getSkillsToLearn() {
    return skillsToLearn.stream()
        .filter(s -> !s.getIsTeachingSkill())
        .map(UserSkill::getSkill)
        .toList();
  }
}
