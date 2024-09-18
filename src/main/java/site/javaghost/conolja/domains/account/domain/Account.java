package site.javaghost.conolja.domains.account.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Account {
  @Id
  private Long id;
  private String username;
  private String password;
  private String name;

  @Builder
  private Account(String username, String password, String name) {
    this.username = username;
    this.password = password;
    this.name = name;
  }
}
