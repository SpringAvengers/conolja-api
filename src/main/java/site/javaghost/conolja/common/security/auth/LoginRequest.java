package site.javaghost.conolja.common.security.auth;

import lombok.Builder;

@Builder
public record LoginRequest(
        String username,
        String password
) {
}
