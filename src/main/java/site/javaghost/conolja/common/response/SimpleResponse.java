package site.javaghost.conolja.common.response;

public record SimpleResponse(
  String message
) {
  public static SimpleResponse of(String message) {
    return new SimpleResponse(message);
  }
}
