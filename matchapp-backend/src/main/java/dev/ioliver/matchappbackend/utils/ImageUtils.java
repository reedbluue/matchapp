package dev.ioliver.matchappbackend.utils;

import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import java.io.ByteArrayOutputStream;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

public abstract class ImageUtils {
  public static byte[] processAvatarImage(MultipartFile multipartFile) throws BadRequestException {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      Thumbnails.of(multipartFile.getResource().getInputStream())
          .size(1080, 1080)
          .outputQuality(0.65f)
          .toOutputStream(byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();
    } catch (Exception e) {
      throw new BadRequestException("Error saving this image. Please try with a different image.");
    }
  }
}
