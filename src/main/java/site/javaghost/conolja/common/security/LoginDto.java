package site.javaghost.conolja.common.security;

import lombok.Builder;

@Builder
public record LoginDto(
        String username,
        Long id,
        String name
) {
}
