package site.javaghost.conolja.domains.account.presentation;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.javaghost.conolja.domains.account.application.AccountService;
import site.javaghost.conolja.domains.account.presentation.dto.AccountCreateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@Tag(name = "계정", description = "계정 관리")
public class AccountController {

	private final AccountService accountService;


}
