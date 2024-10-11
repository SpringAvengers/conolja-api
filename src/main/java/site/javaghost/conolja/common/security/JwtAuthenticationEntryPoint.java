package site.javaghost.conolja.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    log.error("JwtAuthenticationEntryPoint : {}", authException.getMessage());
    log.info("request: {}", request);
    log.info("request.getRequestURI: {}", request.getRequestURI());
    log.info("request.getPathInfo(): {}", request.getPathInfo());
    ObjectMapper objectMapper = new ObjectMapper();
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");
    response.getWriter().write("인증되지 않은 사용자 : 401 Error.");
    response.getWriter().write("request.getPathInfo(): " + request.getPathInfo());
    response.getWriter().write("request.getRequestURI(): " + request.getRequestURI());
  }
}
