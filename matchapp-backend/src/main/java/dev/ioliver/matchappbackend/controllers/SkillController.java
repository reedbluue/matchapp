package dev.ioliver.matchappbackend.controllers;

import dev.ioliver.matchappbackend.dtos.skill.SkillCreateDto;
import dev.ioliver.matchappbackend.dtos.skill.SkillDto;
import dev.ioliver.matchappbackend.dtos.skill.SkillDtoListWithArea;
import dev.ioliver.matchappbackend.dtos.skill.SkillUpdateDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.services.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skill")
@Tag(name = "Skill Controller")
@SecurityRequirement(name = "Bearer")
public class SkillController {
  private final SkillService skillService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "This endpoint is used to get all skill", summary = "Get all skill")
  public List<SkillDto> getAllSkills() {
    return skillService.findAll();
  }

  @GetMapping("/separated-by-area")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "This endpoint is used to get all skill separated by area",
      summary = "Get all skill separated by area")
  public List<SkillDtoListWithArea> getAllSkillsSeparatedByArea() {
    return skillService.findAllSeparatedByArea();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(description = "This endpoint is used to create a new skill",
      summary = "Create a new skill")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public SkillDto createSkill(@RequestBody @Valid SkillCreateDto dto) throws BadRequestException {
    return skillService.create(dto);
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "This endpoint is used to update a skill", summary = "Update a skill")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public SkillDto updateSkill(@RequestBody @Valid SkillUpdateDto dto) throws BadRequestException {
    return skillService.update(dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "This endpoint is used to delete a skill", summary = "Delete a skill")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public void deleteSkill(@PathVariable Long id) throws BadRequestException {
    skillService.deleteById(id);
  }
}