package dev.ioliver.matchappbackend.repositories;

import dev.ioliver.matchappbackend.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  @Query("""
          SELECT u FROM users u WHERE u IN (
            SELECT us.user FROM user_skill us WHERE us.isTeachingSkill = TRUE AND us.skill IN (
              SELECT ust.skill FROM user_skill ust WHERE ust.user = ?1 AND ust.isTeachingSkill = FALSE
            )
          ) AND u IN (
            SELECT us.user FROM user_skill us WHERE us.isTeachingSkill = FALSE AND us.skill IN (
              SELECT ust.skill FROM user_skill ust WHERE ust.user = ?1 AND ust.isTeachingSkill = TRUE
            ) AND (
              SELECT COUNT(m) FROM matches m
              WHERE(m.userInfo1.user = ?1 AND m.userInfo2.user = u) OR (m.userInfo1.user = u
              AND m.userInfo2.user = ?1 AND (m.userInfo2.status != dev.ioliver.matchappbackend.enums.MatchStatus.WAITING
              AND m.userInfo2.status != dev.ioliver.matchappbackend.enums.MatchStatus.REJECTED))
            ) <= 0
          )
      """)
  List<User> findCompatibleUsers(User user);
}
