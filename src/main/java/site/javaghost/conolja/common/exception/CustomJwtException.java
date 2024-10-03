package site.javaghost.conolja.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomJwtException extends RuntimeException {
  
  private final HttpStatus httpStatus;
  
  public CustomJwtException(String message) {
    super(message);
    this.httpStatus = HttpStatus.UNAUTHORIZED; // 기본 상태 코드 설정
  }
  
  public CustomJwtException(String message, Throwable cause) {
    super(message, cause);
    this.httpStatus = HttpStatus.UNAUTHORIZED;
  }
  
  public CustomJwtException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }
  
  public CustomJwtException(String message, Throwable cause, HttpStatus httpStatus) {
    super(message, cause);
    this.httpStatus = httpStatus;
  }
}
