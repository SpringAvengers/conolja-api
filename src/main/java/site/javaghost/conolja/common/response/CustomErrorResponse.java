package site.javaghost.conolja.common.response;

import lombok.Builder;
import site.javaghost.conolja.common.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CustomErrorResponse(
  String path,
  ErrorCode errorCode,
  LocalDateTime timestamp,
  String message,
  List<Object> details
) {

  public static CustomErrorResponse withSingleDetail(String path, ErrorCode errorCode, LocalDateTime timestamp, Object detail) {
    return withDetails(path, errorCode, timestamp, List.of(detail));
  }

  public static CustomErrorResponse withDetails(String path, ErrorCode errorCode, LocalDateTime timestamp, List<Object> details) {
    return CustomErrorResponse.builder()
      .path(path)
      .errorCode(errorCode)
      .timestamp(timestamp)
      .message(errorCode.getMessage())
      .details(details)
      .build();
  }
}
