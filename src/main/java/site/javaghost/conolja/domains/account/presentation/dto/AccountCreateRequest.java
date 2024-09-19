package site.javaghost.conolja.domains.account.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import site.javaghost.conolja.domains.account.application.AccountCreateCommand;

@Builder
public record AccountCreateRequest(
    @Email @NotBlank
    @Schema(description = "이메일", example = "test@email.com")
    String email,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Schema(description = "비밀번호", example = "1234")
    String password,
    @NotBlank
    @Schema(description = "이름", example = "홍길동")
    String name
) {
    public AccountCreateCommand toCommand() {
        return AccountCreateCommand.builder()
            .email(email)
            .password(password)
            .name(name)
            .build();
    }
}
