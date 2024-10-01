package site.javaghost.conolja.common.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

      final String authHeader = request.getHeader(jwtProperties.header());

      try {
        // 인증이 필요없는 경우 (Authorization 헤더 없는 경우)
        if(containsAuthHeader(authHeader)){
            return;
        }

        final String accessToken = jwtTokenUtil.parseToken(authHeader);

        // JWT 토큰이 유효하면 Authentication 꺼내서 SecurityContext 에 인증 정보를 저장
        if (accessToken != null && jwtTokenUtil.isTokenValid(accessToken)) {
          Authentication auth = jwtTokenUtil.getAuthentication(accessToken);
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (Exception e) {
        log.error("JWT 토큰 검증 오류", e);
      } finally {
        filterChain.doFilter(request, response);
      }
    }

    private boolean containsAuthHeader(String authHeader) {
      return authHeader == null || !authHeader.startsWith(jwtProperties.prefix());
    }
}
