package dev.ioliver.matchappbackend.controllers;

import dev.ioliver.matchappbackend.dtos.match.MatchAcceptDto;
import dev.ioliver.matchappbackend.dtos.match.MatchBasicDto;
import dev.ioliver.matchappbackend.dtos.match.MatchRejectDto;
import dev.ioliver.matchappbackend.enums.MatchResult;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.models.security.UserDetailsImp;
import dev.ioliver.matchappbackend.services.MatchService;
import dev.ioliver.matchappbackend.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Match Controller")
@RequestMapping("/api/match")
@SecurityRequirement(name = "Bearer")
public class MatchController {
  private final MatchService matchService;

  @GetMapping("/accepted")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "This endpoint is used to list all accepted matches",
      summary = "List all accepted matches by user")
  public List<MatchBasicDto> listAllAcceptedMatchesByUser(
      UsernamePasswordAuthenticationToken token) {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(token);
    return matchService.listAllAcceptedMatchesByUserId(userDetailsImp.getUser().id());
  }

  @PostMapping("/accept")
  @Operation(description = "This endpoint is used to accept a match", summary = "Accept a match")
  @ApiResponse(responseCode = "201", description = "Created", content = @Content())
  @ApiResponse(responseCode = "204", description = "No Content", content = @Content())
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public ResponseEntity<MatchResult> acceptMatch(@RequestBody MatchAcceptDto dto,
      UsernamePasswordAuthenticationToken token) throws BadRequestException {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(token);
    MatchResult matchResult = matchService.acceptMatch(userDetailsImp.getUser(), dto);
    if (matchResult == MatchResult.CREATED) {
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } else {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
  }

  @PostMapping("/reject")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "This endpoint is used to reject a match", summary = "Reject a match")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public void rejectMatch(@RequestBody MatchRejectDto dto,
      UsernamePasswordAuthenticationToken token) throws BadRequestException {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(token);
    matchService.rejectMatch(userDetailsImp.getUser(), dto);
  }
}