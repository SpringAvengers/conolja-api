package site.javaghost.conolja.domains.account.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.javaghost.conolja.common.security.AccountRole;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Account {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @Builder
    private Account(String username, String password, String name, AccountRole role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public static Account create(String username, String password, String name, AccountRole role) {
        return Account.builder()
                .username(username)
                .password(password)
                .name(name)
                .role(role)
                .build();
    }
}
