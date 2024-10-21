package site.javaghost.conolja.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import site.javaghost.conolja.common.response.CustomErrorResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (JwtAuthenticationException e) {
      //토큰의 유효기간 만료
      setErrorResponse(response, request.getRequestURI(), e.getErrorCode());
    } catch (Exception e) {
      // 서버 에러
      setErrorResponse(response, request.getRequestURI(), ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  private void setErrorResponse(HttpServletResponse response, String path, ErrorCode errorCode) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    response.setStatus(errorCode.getStatus());
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");
    String body = objectMapper.writeValueAsString(
      CustomErrorResponse.withDetails(path, errorCode, LocalDateTime.now(), List.of())
    );
    response.getWriter().write(body);
  }
}
