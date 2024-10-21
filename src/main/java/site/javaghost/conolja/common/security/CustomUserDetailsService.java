package site.javaghost.conolja.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.javaghost.conolja.common.exception.ErrorCode;
import site.javaghost.conolja.domains.account.domain.Account;
import site.javaghost.conolja.domains.account.infra.AccountJpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountJpaRepository accountJpaRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountJpaRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(ErrorCode.INVALID_USERNAME.getMessage()));
        return User.withUserDetails(
                        CustomUserDetails.create(
                                account.getUsername(),
                                account.getPassword(),
                                List.of(new SimpleGrantedAuthority(account.getRole().name()))
                        )
                )
                .build();
    }
}
