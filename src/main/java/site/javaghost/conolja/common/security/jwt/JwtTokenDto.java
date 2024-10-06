package site.javaghost.conolja.common.security.jwt;

import lombok.Builder;

@Builder
public record JwtTokenDto(
  String accessToken,
  String refreshToken,
  String role
) {
  public static JwtTokenDto by(String accessToken, String refreshToken, String role) {
    return JwtTokenDto.builder()
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .role(role)
      .build();
  }
}
