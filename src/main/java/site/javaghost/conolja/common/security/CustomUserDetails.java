package site.javaghost.conolja.common.security;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;

  @Builder
  private CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserDetails create(String username, String password, List<GrantedAuthority> authorities) {
    return CustomUserDetails.builder()
      .username(username)
      .password(password)
      .authorities(authorities)
      .build();
  }
}
