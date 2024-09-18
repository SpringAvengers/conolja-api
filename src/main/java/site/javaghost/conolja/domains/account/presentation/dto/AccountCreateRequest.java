package site.javaghost.conolja.domains.account.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountCreateRequest(
    @Email @NotBlank
    String email,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    String password,
    @NotBlank
    String name
) {
}
