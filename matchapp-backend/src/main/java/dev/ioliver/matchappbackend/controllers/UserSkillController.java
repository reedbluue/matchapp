package dev.ioliver.matchappbackend.controllers;

import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.models.security.UserDetailsImp;
import dev.ioliver.matchappbackend.services.UserSkillService;
import dev.ioliver.matchappbackend.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "UserSkill Controller")
@RequestMapping("/api/user-skill")
@SecurityRequirement(name = "Bearer")
public class UserSkillController {
  private final UserSkillService userSkillService;

  @PutMapping("/teach")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "This endpoint is used to update teach skills",
      summary = "Update teach skills")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public void updateTeachSkills(@RequestParam List<Long> skillsIds,
      UsernamePasswordAuthenticationToken token) throws BadRequestException {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(token);
    userSkillService.updateTeachSkills(userDetailsImp.getUser(), skillsIds);
  }

  @PutMapping("/learn")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "This endpoint is used to update learn skills",
      summary = "Update learn skills")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public void updateLearnSkills(@RequestParam List<Long> skillsIds,
      UsernamePasswordAuthenticationToken token) throws BadRequestException {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(token);
    userSkillService.updateLearnSkills(userDetailsImp.getUser(), skillsIds);
  }
}