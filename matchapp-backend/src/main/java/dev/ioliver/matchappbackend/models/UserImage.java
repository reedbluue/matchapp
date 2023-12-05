package dev.ioliver.matchappbackend.models;

import dev.ioliver.matchappbackend.enums.ImageType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.index.Indexed;

@Data
@Builder
@Document(collection = "user_images")
public class UserImage {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Indexed private Long userId;

  private byte[] image;

  @Builder.Default private ImageType type = ImageType.COVER;
}
