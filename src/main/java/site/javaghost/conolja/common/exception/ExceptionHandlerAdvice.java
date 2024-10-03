package site.javaghost.conolja.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {
  @ExceptionHandler(Exception.class)
  public String handleInternalServerException(Exception e) {
    log.error("GlobalExceptionHandler : {}", e.getMessage());
    return e.getMessage();
  }
}
