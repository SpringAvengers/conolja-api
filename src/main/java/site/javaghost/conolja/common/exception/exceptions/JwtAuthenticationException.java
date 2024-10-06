package site.javaghost.conolja.common.exception.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import site.javaghost.conolja.common.exception.ErrorCode;

import static site.javaghost.conolja.common.exception.ErrorCode.*;

@Getter
public class JwtAuthenticationException extends AuthenticationException {
  private ErrorCode errorCode;
  private Object details;

  @Builder
  private JwtAuthenticationException(String message, ErrorCode errorCode, Object details) {
    super(message);
    this.errorCode = errorCode;
    this.details = details;
  }

  public static JwtAuthenticationException of(BadCredentialsException e) {
    return JwtAuthenticationException.builder()
      .errorCode(INVALID_PASSWORD)
      .message(e.getMessage())
      .build();
  }

  public static JwtAuthenticationException of(UsernameNotFoundException e) {
    return JwtAuthenticationException.builder()
      .errorCode(INVALID_USERNAME)
      .message(e.getMessage())
      .build();
  }

  public static JwtAuthenticationException invalidToken() {
    return JwtAuthenticationException.builder()
      .message(JWT_INVALID_TOKEN.getMessage())
      .errorCode(JWT_INVALID_TOKEN)
      .build();
  }

  public static JwtAuthenticationException tokenExpired() {
    return JwtAuthenticationException.builder()
      .message(JWT_TOKEN_EXPIRED.getMessage())
      .errorCode(JWT_TOKEN_EXPIRED)
      .build();
  }

  public static JwtAuthenticationException invalidAuthorizationHeader(String headerValue) {
    return JwtAuthenticationException.builder()
      .message(JWT_INVALID_AUTHORIZATION_HEADER.getMessage() + "전달 된 값: " + headerValue)
      .errorCode(JWT_INVALID_AUTHORIZATION_HEADER)
      .build();
  }

  public static JwtAuthenticationException invalidType(String headerValue) {
    return JwtAuthenticationException.builder()
      .message(JWT_INVALID_TYPE.getMessage() + "전달 된 값: " + headerValue)
      .errorCode(JWT_INVALID_TYPE)
      .build();
  }
}
