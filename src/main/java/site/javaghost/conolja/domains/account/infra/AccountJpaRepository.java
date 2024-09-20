package site.javaghost.conolja.domains.account.infra;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import site.javaghost.conolja.domains.account.domain.Account;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByUsername(String username);

	Optional<Account> findByName(String name);
}
