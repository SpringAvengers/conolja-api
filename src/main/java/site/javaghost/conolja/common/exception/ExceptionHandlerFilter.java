package site.javaghost.conolja.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import site.javaghost.conolja.common.exception.exceptions.JwtAuthenticationException;
import site.javaghost.conolja.common.response.CustomErrorResponse;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try{
      filterChain.doFilter(request, response);
    } catch (JwtAuthenticationException e){
      //토큰의 유효기간 만료
      setErrorResponse(response, request.getRequestURI(), e.getErrorCode());
    } catch (Exception e){
      // 서버 에러
      setErrorResponse(response, request.getRequestURI(), ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  private void setErrorResponse(HttpServletResponse response, String path, ErrorCode e) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    response.setStatus(e.getStatus());
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(CustomErrorResponse.create(path, e)));
  }
}
