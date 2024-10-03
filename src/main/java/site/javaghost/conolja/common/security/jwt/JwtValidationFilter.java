package site.javaghost.conolja.common.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
    // Authorization 헤더가 없는 경우 (ex. 로그인 요청)
    if(containsAuthHeader(authHeader)){
      filterChain.doFilter(request, response);
      return;
    }

    final String accessToken = jwtTokenUtil.parseToken(authHeader);

    // 토큰이 없거나 만료 된 경우 예외 처리
    if (!isValid(accessToken)) {
      throw new AuthenticationServiceException("JWT 토큰 검증 오류");
    }

    filterChain.doFilter(request, response);
  }

  private boolean isValid(String accessToken) {
    return StringUtils.hasText(accessToken) && jwtTokenUtil.isTokenValid(accessToken);
  }

  private boolean containsAuthHeader(String authHeader) {
    return !StringUtils.hasText(authHeader) || !authHeader.startsWith(jwtProperties.prefix());
  }
}
