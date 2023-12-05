package dev.ioliver.matchappbackend.repositories;

import dev.ioliver.matchappbackend.models.UserImage;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserImageRepository extends MongoRepository<UserImage, Long> {

  @Query("{'userId': ?0, 'type': 'PROFILE'}")
  Optional<UserImage> findProfileByUserId(Long userId);
}
