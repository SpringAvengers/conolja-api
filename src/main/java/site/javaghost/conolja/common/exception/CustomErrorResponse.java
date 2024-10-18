package site.javaghost.conolja.common.exception;

public record CustomErrorResponse<T>(
  String path,
  ErrorCode errorCode,
  String message,
  T details
) {

  public static <T> CustomErrorResponse<T> create(String path, ErrorCode errorCode) {
    return CustomErrorResponse.create(path, errorCode, null);
  }

  public static <T> CustomErrorResponse<T> create(String path, ErrorCode errorCode, T details) {
    return new CustomErrorResponse<>(path, errorCode, errorCode.getMessage(), details);
  }
}
