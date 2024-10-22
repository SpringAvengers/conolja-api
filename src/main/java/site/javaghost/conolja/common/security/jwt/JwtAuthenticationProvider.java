package site.javaghost.conolja.common.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import site.javaghost.conolja.common.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication auth) throws AuthenticationException {
    UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getName());
    if (!passwordEncoder.matches((CharSequence) auth.getCredentials(), userDetails.getPassword())) {
      throw new BadCredentialsException(ErrorCode.INVALID_PASSWORD.getMessage());
    }
    // 비밀번호 정보를 제거
    if (userDetails instanceof CredentialsContainer) {
      ((CredentialsContainer) userDetails).eraseCredentials();
    }
    return UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
