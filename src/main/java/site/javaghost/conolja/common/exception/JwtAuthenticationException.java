package site.javaghost.conolja.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static site.javaghost.conolja.common.exception.ErrorCode.INVALID_PASSWORD;
import static site.javaghost.conolja.common.exception.ErrorCode.INVALID_USERNAME;
import static site.javaghost.conolja.common.exception.ErrorCode.JWT_INVALID_AUTHORIZATION_HEADER;
import static site.javaghost.conolja.common.exception.ErrorCode.JWT_INVALID_TOKEN;
import static site.javaghost.conolja.common.exception.ErrorCode.JWT_INVALID_TYPE;
import static site.javaghost.conolja.common.exception.ErrorCode.JWT_TOKEN_EXPIRED;

@Getter
public class JwtAuthenticationException extends AuthenticationException {
  private final ErrorCode errorCode;
  private final LocalDateTime timestamp;
  private final List<Object> details;

  @Builder
  private JwtAuthenticationException(String message, ErrorCode errorCode, LocalDateTime timestamp, List<Object> details) {
    super(message);
    this.errorCode = errorCode;
    this.timestamp = timestamp;
    this.details = details;
  }

  public static JwtAuthenticationException badCredentials(BadCredentialsException e) {
    return JwtAuthenticationException.builder()
      .errorCode(INVALID_PASSWORD)
      .timestamp(LocalDateTime.now())
      .message(e.getMessage())
      .build();
  }

  public static JwtAuthenticationException usernameNotFound(UsernameNotFoundException e) {
    return JwtAuthenticationException.builder()
      .errorCode(INVALID_USERNAME)
      .timestamp(LocalDateTime.now())
      .message(e.getMessage())
      .build();
  }

  public static JwtAuthenticationException invalidToken() {
    return JwtAuthenticationException.builder()
      .message(JWT_INVALID_TOKEN.getMessage())
      .timestamp(LocalDateTime.now())
      .errorCode(JWT_INVALID_TOKEN)
      .build();
  }

  public static JwtAuthenticationException tokenExpired() {
    return JwtAuthenticationException.builder()
      .message(JWT_TOKEN_EXPIRED.getMessage())
      .timestamp(LocalDateTime.now())
      .errorCode(JWT_TOKEN_EXPIRED)
      .build();
  }

  public static JwtAuthenticationException invalidAuthorizationHeader(String headerValue) {
    return JwtAuthenticationException.builder()
      .message(JWT_INVALID_AUTHORIZATION_HEADER.getMessage() + "(Authorization: " + headerValue + ")")
      .timestamp(LocalDateTime.now())
      .errorCode(JWT_INVALID_AUTHORIZATION_HEADER)
      .build();
  }

  public static JwtAuthenticationException invalidType(String headerValue) {
    return JwtAuthenticationException.builder()
      .message(JWT_INVALID_TYPE.getMessage() + "전달 된 값: " + headerValue)
      .timestamp(LocalDateTime.now())
      .errorCode(JWT_INVALID_TYPE)
      .build();
  }
}
