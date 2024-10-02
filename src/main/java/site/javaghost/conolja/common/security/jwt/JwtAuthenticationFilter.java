package site.javaghost.conolja.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import site.javaghost.conolja.common.security.CustomUserDetails;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtTokenUtil jwtTokenUtil;
  private final JwtProperties jwtProperties;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Builder
  public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, JwtProperties jwtProperties) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.jwtProperties = jwtProperties;
    setFilterProcessesUrl("/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

    String username = "";
    String password = "";

    try {
      // 헤더에서 accessToken 추출
      LoginRequest loginRequest = objectMapper.readValue(wrappedRequest.getInputStream(), LoginRequest.class);
      username = loginRequest.username();
      password = loginRequest.password();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // accessToken 을 이용해 인증 정보 생성 (아직 인증 전 단계)
    UsernamePasswordAuthenticationToken preAuthToken =
      UsernamePasswordAuthenticationToken.unauthenticated(CustomUserDetails.create(username, password, List.of()), password);

    // authenticationProvider 를 통해 검증 '된' 인증 정보 생성
    Authentication authenticated = getAuthenticationManager().authenticate(preAuthToken);

    // SecurityContextHolder 에 인증 정보 저장
    SecurityContextHolder.getContext().setAuthentication(authenticated);
    return authenticated;
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

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    log.error("Authentication failed: {}", failed.getMessage());

    // 인증 실패 상태 코드 설정
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    // 응답 메시지 작성
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("{ \"error\": \"" + failed.getMessage() + "\" }");
  }
}
