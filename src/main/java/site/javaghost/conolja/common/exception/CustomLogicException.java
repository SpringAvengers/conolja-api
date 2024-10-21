package site.javaghost.conolja.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomLogicException extends RuntimeException {
  private final ErrorCode errorCode;
  private final LocalDateTime timestamp;
  private final List<Object> details;

  @Builder
  public CustomLogicException(ErrorCode errorCode, LocalDateTime timestamp, List<Object> details) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.timestamp = timestamp;
    this.details = details;
  }

  public static CustomLogicException defaults(ErrorCode errorCode) {
    return CustomLogicException.builder()
      .errorCode(errorCode)
      .timestamp(LocalDateTime.now())
      .details(new ArrayList<>())
      .build();
  }

  public static CustomLogicException withDetails(ErrorCode errorCode, List<Object> details) {
    return CustomLogicException.builder()
      .errorCode(errorCode)
      .timestamp(LocalDateTime.now())
      .details(details)
      .build();
  }

  public boolean hasDetails() {
    return !details.isEmpty();
  }
}
