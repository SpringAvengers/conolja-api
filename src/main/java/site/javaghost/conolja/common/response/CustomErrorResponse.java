package site.javaghost.conolja.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.javaghost.conolja.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public class CustomErrorResponse {

  private String path;
  private String message;
  private int status;
  private Object details;

  @Builder
  private CustomErrorResponse(String path, String message, int status, Object details) {
    this.path = path;
    this.message = message;
    this.status = status;
    this.details = details;
  }

  public static CustomErrorResponse create(String path, ErrorCode errorCode) {
    return CustomErrorResponse.builder()
      .path(path)
      .status(errorCode.getStatus())
      .message(errorCode.getMessage())
      .build();
  }
}
