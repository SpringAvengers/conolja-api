package site.javaghost.conolja.common.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtTokenUtil jwtTokenUtil;
  private final JwtProperties jwtProperties;

  @Builder
  public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, JwtProperties jwtProperties) {
      this.jwtTokenUtil = jwtTokenUtil;
      this.jwtProperties = jwtProperties;
      setFilterProcessesUrl("/auth/login");
  }

  @Override
  protected String obtainPassword(HttpServletRequest request) {
    Authentication auth = getAuthentication(request.getHeader(jwtProperties.header()));
    return (String) auth.getCredentials();
  }

  @Override
  protected String obtainUsername(HttpServletRequest request) {
    Authentication auth = getAuthentication(request.getHeader(jwtProperties.header()));
    return auth.getName();
  }

  private Authentication getAuthentication(String headerValue) {
    String accessToken = jwtTokenUtil.parseToken(headerValue);
    return jwtTokenUtil.getAuthentication(accessToken);
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult) throws IOException, ServletException {

    // 토큰 생성
    JwtTokenDto jwtTokenDto = jwtTokenUtil.generateToken(authResult);
    // 토큰을 응답 헤더에 추가
    response.addHeader(jwtProperties.header(), jwtProperties.prefix() + " " + jwtTokenDto.accessToken());
    chain.doFilter(request, response);
  }
}
