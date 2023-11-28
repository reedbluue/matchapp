package dev.ioliver.matchappbackend.filters;

import dev.ioliver.matchappbackend.exceptions.UnauthorizedException;
import dev.ioliver.matchappbackend.models.security.UserDetailsImp;
import dev.ioliver.matchappbackend.services.security.UserDetailsServiceImp;
import dev.ioliver.matchappbackend.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImp userDetailsServiceImp;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    Optional<JwtUtil.TokenDataDto> jwtDataOpt = extractJwtData(request);
    if (jwtDataOpt.isEmpty() || jwtDataOpt.get().isRefreshToken()) {
      filterChain.doFilter(request, response);
      return;
    }

    JwtUtil.TokenDataDto tokenData = jwtDataOpt.get();

    UserDetailsImp userDetailsImp = userDetailsServiceImp.loadUserByUsername(tokenData.email());

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userDetailsImp, null,
            userDetailsImp.getAuthorities());

    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    filterChain.doFilter(request, response);
  }

  private Optional<JwtUtil.TokenDataDto> extractJwtData(HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");
    if (authorization != null) {
      try {
        return Optional.of(jwtUtil.validateToken(authorization.replace("Bearer ", "")));
      } catch (UnauthorizedException e) {
        return Optional.empty();
      }
    }
    return Optional.empty();
  }
}
