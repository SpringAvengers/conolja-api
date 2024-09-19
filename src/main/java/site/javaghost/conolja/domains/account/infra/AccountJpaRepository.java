package site.javaghost.conolja.domains.account.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import site.javaghost.conolja.domains.account.domain.Account;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByName(String name);
}
