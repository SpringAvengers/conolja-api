package site.javaghost.conolja.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.javaghost.conolja.common.response.CustomErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(JwtAuthenticationException.class)
  public CustomErrorResponse handleJwtAuthenticationException(JwtAuthenticationException e, HttpServletRequest request) {
    log.error("ExceptionHandlerAdvice: ", e);
    return CustomErrorResponse.withDetails(
      request.getRequestURI(),
      e.getErrorCode(),
      e.getTimestamp(),
      List.of(e.getMessage()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(CustomLogicException.class)
  public CustomErrorResponse handleCustomLogicException(CustomLogicException e, HttpServletRequest request) {
    log.error("ExceptionHandlerAdvice: ", e);
    return CustomErrorResponse.withDetails(
      request.getRequestURI(),
      e.getErrorCode(),
      e.getTimestamp(),
      e.getDetails());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalStateException.class)
  public CustomErrorResponse handleIllegalStateException(IllegalStateException e, HttpServletRequest request) {
    log.error("ExceptionHandlerAdvice: ", e);
    return CustomErrorResponse.withDetails(
      request.getRequestURI(),
      ErrorCode.BAD_REQUEST,
      LocalDateTime.now(),
      List.of(e.getMessage()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public CustomErrorResponse handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
    log.error("ExceptionHandlerAdvice: ", e);
    return CustomErrorResponse.withDetails(
      request.getRequestURI(),
      ErrorCode.BAD_REQUEST,
      LocalDateTime.now(),
      List.of(e.getMessage()));
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public CustomErrorResponse handleInternalServerException(Exception e, HttpServletRequest request) {
    log.error("ExceptionHandlerAdvice: ", e);
    return CustomErrorResponse.withDetails(
      request.getRequestURI(),
      ErrorCode.INTERNAL_SERVER_ERROR,
      LocalDateTime.now(),
      List.of());
  }
}
