package dev.ioliver.matchappbackend.controllers;

import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaCreateDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaDto;
import dev.ioliver.matchappbackend.dtos.skillArea.SkillAreaUpdateDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.services.SkillAreaService;
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
@RequestMapping("/api/skill-area")
@SecurityRequirement(name = "Bearer")
@Tag(name = "SkillArea Controller")
public class SkillAreaController {
  private final SkillAreaService skillAreaService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "This endpoint is used to get all skill areas",
      summary = "Get all skill areas")
  public List<SkillAreaDto> getAllSkillAreas() {
    return skillAreaService.findAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(description = "This endpoint is used to create a new skill area",
      summary = "Create a new skill area")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public SkillAreaDto createSkillArea(@RequestBody @Valid SkillAreaCreateDto dto)
      throws BadRequestException {
    return skillAreaService.create(dto);
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "This endpoint is used to update a skill area",
      summary = "Update a skill area")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public SkillAreaDto updateSkillArea(@RequestBody @Valid SkillAreaUpdateDto dto)
      throws BadRequestException {
    return skillAreaService.update(dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "This endpoint is used to delete a skill area",
      summary = "Delete a skill area")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public void deleteSkillArea(@PathVariable Long id) throws BadRequestException {
    skillAreaService.deleteById(id);
  }
}