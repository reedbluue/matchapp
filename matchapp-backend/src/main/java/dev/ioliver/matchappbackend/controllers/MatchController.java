package dev.ioliver.matchappbackend.controllers;

import dev.ioliver.matchappbackend.dtos.match.MatchCreateDto;
import dev.ioliver.matchappbackend.dtos.match.MatchDto;
import dev.ioliver.matchappbackend.exceptions.BadRequestException;
import dev.ioliver.matchappbackend.models.security.UserDetailsImp;
import dev.ioliver.matchappbackend.services.MatchService;
import dev.ioliver.matchappbackend.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/match")
@SecurityRequirement(name = "Bearer")
public class MatchController {
  private final MatchService matchService;

  @GetMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(description = "This endpoint is used to list all matches",
      summary = "List all matches by user")
  public List<MatchDto> listAllMatchesByUser(UsernamePasswordAuthenticationToken token) {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(token);
    return matchService.listAllMatchesByUserId(userDetailsImp.getUser().id());
  }

  @GetMapping("/waiting")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "This endpoint is used to list all waiting matches",
      summary = "List all waiting matches by user")
  public List<MatchDto> listAllWaitingMatchesByUser(UsernamePasswordAuthenticationToken token) {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(token);
    return matchService.listAllWaitingMatchesByUserId(userDetailsImp.getUser().id());
  }

  @GetMapping("/accepted")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "This endpoint is used to list all accepted matches",
      summary = "List all accepted matches by user")
  public List<MatchDto> listAllAcceptedMatchesByUser(UsernamePasswordAuthenticationToken token) {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(token);
    return matchService.listAllAcceptedMatchesByUserId(userDetailsImp.getUser().id());
  }

  @PostMapping("/accept/{matchId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "This endpoint is used to accept a match", summary = "Accept a match")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public void acceptMatch(@PathVariable Long matchId, UsernamePasswordAuthenticationToken token)
      throws BadRequestException {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(token);
    matchService.acceptMatch(matchId, userDetailsImp.getUser().id());
  }

  @PostMapping("/reject/{matchId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "This endpoint is used to reject a match", summary = "Reject a match")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public void rejectMatch(@PathVariable Long matchId, UsernamePasswordAuthenticationToken token)
      throws BadRequestException {
    UserDetailsImp userDetailsImp = AuthUtil.toUserDetailsImp(token);
    matchService.rejectMatch(matchId, userDetailsImp.getUser().id());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(description = "This endpoint is used to create a new match",
      summary = "Create a new match")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
  public MatchDto create(@RequestBody MatchCreateDto dto, UsernamePasswordAuthenticationToken token)
      throws BadRequestException {
    return matchService.create(AuthUtil.toUserDetailsImp(token).getUser(), dto);
  }
}