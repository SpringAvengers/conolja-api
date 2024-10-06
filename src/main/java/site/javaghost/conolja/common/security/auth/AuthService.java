package site.javaghost.conolja.common.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.javaghost.conolja.common.security.jwt.JwtTokenUtil;
import site.javaghost.conolja.domains.account.application.AccountCreateCommand;
import site.javaghost.conolja.domains.account.domain.Account;
import site.javaghost.conolja.domains.account.infra.AccountJpaRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserDetailsService userDetailsService;
  private final AccountJpaRepository accountJpaRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtil jwtTokenUtil;

  @Transactional
  public void signup(AccountCreateCommand command) {
    String username = command.email();
    if (checkDuplicate(username)) {
      throw new IllegalArgumentException("이미 존재하는 계정입니다. - " + username);
    }
    Account account = command.toEntity();
    account.encodePassword(passwordEncoder.encode(command.password()));
    accountJpaRepository.save(account);
  }

  public boolean checkDuplicate(String email) {
    return accountJpaRepository.existsByUsername(email);
  }

  public Authentication issueToken() {
    return null;
  }
}
