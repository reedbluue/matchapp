package dev.ioliver.matchappbackend.repositories;

import dev.ioliver.matchappbackend.models.Skill;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
  Optional<Skill> findByName(String name);

  boolean existsByName(String name);
}
