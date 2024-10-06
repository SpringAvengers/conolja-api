package site.javaghost.conolja.common.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
  String secret,
  String prefix,
  String header,
  String issuer,
  String type,
  @DurationUnit(ChronoUnit.MILLIS)
  Duration expiration,
  Refresh refresh) {

    public record Refresh(
      @DurationUnit(ChronoUnit.MILLIS)
      Duration expiration,
      String header) { }
}
