package site.javaghost.conolja.common.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    return super.attemptAuthentication(request, response);
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
