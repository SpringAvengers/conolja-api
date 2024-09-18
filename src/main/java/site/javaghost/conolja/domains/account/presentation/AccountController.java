package site.javaghost.conolja.domains.account.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.javaghost.conolja.domains.account.presentation.dto.AccountCreateRequest;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

  @PostMapping("/signup")
  public ResponseEntity<Map<String, String>> signup(
    @Valid @RequestBody AccountCreateRequest request) {

    return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
  }
}
