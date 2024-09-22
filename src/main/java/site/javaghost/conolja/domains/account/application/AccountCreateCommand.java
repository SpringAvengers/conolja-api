package site.javaghost.conolja.domains.account.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import site.javaghost.conolja.common.security.AccountRole;
import site.javaghost.conolja.domains.account.domain.Account;

@Builder
public record AccountCreateCommand(
        @Email @NotBlank String email,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotBlank String password,
        @NotBlank String name,
        @NotBlank AccountRole role) {
    public Account toEntity() {
        return Account.create(email, password, name, role);
    }
}
