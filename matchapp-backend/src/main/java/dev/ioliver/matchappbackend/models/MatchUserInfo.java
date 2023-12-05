package dev.ioliver.matchappbackend.models;

import dev.ioliver.matchappbackend.enums.MatchStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "match_user_info")
public class MatchUserInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne private User user;

  @Builder.Default private MatchStatus status = MatchStatus.WAITING;

  @ManyToOne private Skill skillToTeach;
}
