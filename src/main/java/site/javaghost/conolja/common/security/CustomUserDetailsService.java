package site.javaghost.conolja.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.javaghost.conolja.domains.account.domain.Account;
import site.javaghost.conolja.domains.account.domain.repo.AccountRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

  private final AccountRepository accountRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account account = accountRepository.findByUsername(username).orElseThrow(
      () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    return User.withUserDetails(CustomUserDetails.create(account.getUsername(), account.getPassword(), List.of())).build();
  }
}
