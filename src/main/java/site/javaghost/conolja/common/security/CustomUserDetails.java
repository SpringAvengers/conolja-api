package site.javaghost.conolja.common.security;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails, CredentialsContainer {

    private final String username;
    private final String password;
    private Collection<? extends GrantedAuthority> authorities;

    @Builder
    private CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetails create(String username, String password, List<GrantedAuthority> authorities) {
        return CustomUserDetails.builder().username(username).password(password).authorities(authorities).build();
    }

    // 계정 상태 관련 메서드 추가
    @Override
    public boolean isAccountNonExpired() {
        // true 반환 시 계정이 만료되지 않았음을 의미
        return true;  // 실제로 만료 여부를 체크하는 로직을 추가할 수 있음
    }

    @Override
    public boolean isAccountNonLocked() {
        // true 반환 시 계정이 잠기지 않았음을 의미
        return true;  // 실제로 계정 잠금 여부를 체크하는 로직을 추가할 수 있음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // true 반환 시 자격 증명(비밀번호 등)이 만료되지 않았음을 의미
        return true;  // 실제로 자격 증명 만료 여부를 체크하는 로직을 추가할 수 있음
    }

    @Override
    public boolean isEnabled() {
        // true 반환 시 계정이 활성화되어 있음을 의미
        return true;  // 실제로 계정 활성화 여부를 체크하는 로직을 추가할 수 있음
    }

    @Override
    public void eraseCredentials() {
        this.authorities = null;
    }
}
