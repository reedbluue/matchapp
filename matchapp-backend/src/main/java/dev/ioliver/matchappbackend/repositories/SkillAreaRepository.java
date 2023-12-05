package dev.ioliver.matchappbackend.repositories;

import dev.ioliver.matchappbackend.models.SkillArea;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillAreaRepository extends JpaRepository<SkillArea, Long> {
  Optional<SkillArea> findByName(String name);

  boolean existsByName(String name);
}
