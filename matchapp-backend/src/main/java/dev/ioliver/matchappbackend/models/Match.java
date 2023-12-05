package dev.ioliver.matchappbackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "matches")
public class Match {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @Cascade(CascadeType.ALL)
  private MatchUserInfo userInfo1;

  @OneToOne
  @Cascade(CascadeType.ALL)
  private MatchUserInfo userInfo2;
}
