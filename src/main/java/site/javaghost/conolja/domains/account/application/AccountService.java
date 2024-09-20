package site.javaghost.conolja.domains.account.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.javaghost.conolja.domains.account.infra.AccountJpaRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
	private final AccountJpaRepository accountJpaRepository;

	@Transactional
	public void signup(AccountCreateCommand command) {
		accountJpaRepository.save(command.toEntity());
	}
}
