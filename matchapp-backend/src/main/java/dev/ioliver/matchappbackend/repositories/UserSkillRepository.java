package dev.ioliver.matchappbackend.repositories;

import dev.ioliver.matchappbackend.models.UserSkill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
  List<UserSkill> findAllByUser_Id(Long userId);

  void deleteAllByUser_IdAndIsTeachingSkill(Long userId, Boolean isTeachingSkill);

  boolean existsBySkill_IdAndUser_IdAndIsTeachingSkill(Long skillId, Long userId,
      Boolean isTeachingSkill);
}
