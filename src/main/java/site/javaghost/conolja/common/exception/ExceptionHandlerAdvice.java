package site.javaghost.conolja.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public CustomErrorResponse<?> handleInternalServerException(Exception e, HttpServletRequest request) {
    log.error("GlobalExceptionHandler : {}", e.getMessage());
    return CustomErrorResponse.create(request.getRequestURI().intern(), ErrorCode.INTERNAL_SERVER_ERROR);
  }
}
