package site.javaghost.conolja.common.security.jwt;

import lombok.Builder;

@Builder
public record LoginRequest(
        String username,
        String password
) {
}
