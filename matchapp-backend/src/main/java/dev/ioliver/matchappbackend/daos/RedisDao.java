package dev.ioliver.matchappbackend.daos;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisDao {
  private final RedisTemplate<String, String> redisTemplate;

  public void set(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public Optional<String> get(String key) {
    try {
      String value = redisTemplate.opsForValue().get(key);
      if (value == null) return Optional.empty();
      return Optional.of(value);
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
