package dev.ioliver.matchappbackend.repositories;

import dev.ioliver.matchappbackend.models.Match;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchRepository extends JpaRepository<Match, Long> {

  @Query("""
            SELECT m FROM matches m WHERE (m.userInfo1.user.id = ?1 OR m.userInfo2.user.id = ?1)
            AND (m.userInfo1.status = dev.ioliver.matchappbackend.enums.MatchStatus.ACCEPTED
            AND m.userInfo2.status = dev.ioliver.matchappbackend.enums.MatchStatus.ACCEPTED)
      """)
  List<Match> findAllAcceptedByUserId(Long id);

  @Query("""
            SELECT m FROM matches m WHERE (m.userInfo1.user.id = ?1 OR m.userInfo2.user.id = ?1)
              AND m.userInfo2.status = dev.ioliver.matchappbackend.enums.MatchStatus.WAITING
      """)
  List<Match> findAllWaitingByUserId(Long id);

  @Query("""
            SELECT m FROM matches m WHERE (m.userInfo1.user.id = ?1 OR m.userInfo2.user.id = ?1)
      """)
  List<Match> findAllByUserId(Long id);

  @Query("""
      SELECT CASE WHEN COUNT(m)> 0 THEN TRUE ELSE FALSE END FROM matches m WHERE m.userInfo1.id = ?1
      AND m.userInfo2.id = ?2 AND m.userInfo1.skillToTeach.id = ?3 AND m.userInfo2.skillToTeach.id = ?4
      """)
  boolean checkIfMatchExists(Long user1, Long user2, Long skillToTeach, Long skillToLearn);

  @Query("""
      SELECT CASE WHEN COUNT(m)> 0 THEN TRUE ELSE FALSE END FROM matches m WHERE m.id = ?1
      AND (m.userInfo1.status = dev.ioliver.matchappbackend.enums.MatchStatus.ACCEPTED AND
      m.userInfo2.user.id = ?2 AND m.userInfo2.status = dev.ioliver.matchappbackend.enums.MatchStatus.WAITING)
      """)
  boolean checkIfMatchIsAcceptable(Long id, Long userId);
}
