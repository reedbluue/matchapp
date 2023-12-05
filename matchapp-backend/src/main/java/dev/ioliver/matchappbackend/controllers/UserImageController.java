package dev.ioliver.matchappbackend.controllers;

import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.models.security.UserDetailsImp;
import dev.ioliver.matchappbackend.services.UserImageService;
import dev.ioliver.matchappbackend.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "UserImage Controller")
@RequestMapping("/api/user-image")
@SecurityRequirement(name = "Bearer")
public class UserImageController {
  private final UserImageService userImageService;

  @GetMapping(path = "/profile/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  @Operation(description = "This endpoint is used to return the user profile image",
      summary = "Return the user profile image")
  public byte[] findById(@PathVariable Long id) throws BadRequestException {
    return userImageService.getUserProfileImage(id);
  }

  @PostMapping(path = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  @Operation(description = "This endpoint is used to update the user profile image",
      summary = "Update the user profile image")
  public void updateProfileImage(@RequestParam MultipartFile file,
      UsernamePasswordAuthenticationToken authToken) throws BadRequestException {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(authToken);
    userImageService.updateUserProfileImage(userDetailsImp.getUser().id(), file);
  }
}
