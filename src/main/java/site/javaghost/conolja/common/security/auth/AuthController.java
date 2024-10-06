package site.javaghost.conolja.common.security.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.javaghost.conolja.common.security.jwt.JwtTokenDto;
import site.javaghost.conolja.common.security.jwt.JwtTokenUtil;
import site.javaghost.conolja.common.security.jwt.LoginRequest;
import site.javaghost.conolja.domains.account.presentation.dto.AccountCreateRequest;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    public ResponseEntity<Map<String, String>> signup(
      @Valid @RequestBody AccountCreateRequest request) {
        authService.signup(request.toCommand());
        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 합니다.")
    public ResponseEntity<JwtTokenDto> login(
      @Valid @RequestBody LoginRequest loginRequest) {

        // LoginFilter 에서 인증 처리 완료
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtTokenDto jwtTokenDto = jwtTokenUtil.generateToken(auth);
        return ResponseEntity.ok(jwtTokenDto);
    }

    @PostMapping("/re-issue")
    @Operation(summary = "토큰 재발급", description = "토큰을 재발급 합니다.")
    public ResponseEntity<Map<String, String>> issue(IssueRequest request) {

        return ResponseEntity.ok(Map.of("message", "토큰 재발급 성공"));
    }
}
