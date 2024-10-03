package site.javaghost.conolja.common.security.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record LoginRequest(
        @Email @Schema(description = "사용자명", example = "test@email.com", required = true)
        String username,
        @Schema(description = "사용자명", example = "1234", required = true)
        String password
) {
}
