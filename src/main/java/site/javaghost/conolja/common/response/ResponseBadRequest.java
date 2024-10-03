package site.javaghost.conolja.common.response;

import lombok.Builder;

@Builder
public record ResponseBadRequest(
  String field,
  String message,
  Object[] arguments,
  String rejectValue,
  String code,
  String[] codes
) {
}
