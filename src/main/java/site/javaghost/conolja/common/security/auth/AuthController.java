package site.javaghost.conolja.common.security.auth;

import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    public ResponseEntity<Map<String, String>> signup(
      @Valid @RequestBody AccountCreateRequest request) {
        authService.signup(request.toCommand());
        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 합니다.")
    public ResponseEntity<Map<String, String>> login(
      @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(Map.of("message", "로그인 성공"));
    }

    @PostMapping("/issue")
    @Operation(summary = "토큰 재발급", description = "토큰을 재발급 합니다.")
    public ResponseEntity<Map<String, String>> issue(IssueRequest request) {

        return ResponseEntity.ok(Map.of("message", "토큰 재발급 성공"));
    }
}
