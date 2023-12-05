package dev.ioliver.matchappbackend.services;

import dev.ioliver.matchappbackend.enums.ImageType;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.models.UserImage;
import dev.ioliver.matchappbackend.repositories.UserImageRepository;
import dev.ioliver.matchappbackend.utils.ImageUtils;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserImageService {
  private final UserImageRepository userImageRepository;

  @Transactional
  public void updateUserProfileImage(Long id, MultipartFile file) throws BadRequestException {
    if (!isJpg(file)) throw new BadRequestException("Only jpg files are allowed");

    Optional<UserImage> userImage = userImageRepository.findProfileByUserId(id);
    byte[] bytes = ImageUtils.processAvatarImage(file);

    try {
      if (userImage.isPresent()) {
        UserImage image = userImage.get();
        image.setImage(bytes);
        userImageRepository.save(userImage.get());
      } else {
        userImageRepository.save(
            UserImage.builder().userId(id).image(bytes).type(ImageType.PROFILE).build());
      }
    } catch (Exception e) {
      throw new BadRequestException("Error saving this image. Please try with a different image.");
    }
  }

  @Transactional(readOnly = true)
  public byte[] getUserProfileImage(Long id) throws BadRequestException {
    UserImage userImage = userImageRepository.findProfileByUserId(id)
        .orElseThrow(() -> new BadRequestException("No profile image found for this user!"));
    return userImage.getImage();
  }

  private boolean isJpg(MultipartFile file) throws BadRequestException {
    try {
      return Objects.requireNonNull(file.getOriginalFilename()).endsWith(".jpg")
          || Objects.requireNonNull(file.getOriginalFilename()).endsWith(".jpeg");
    } catch (Exception e) {
      throw new BadRequestException("Error saving this image. Please try with a different image.");
    }
  }
}
