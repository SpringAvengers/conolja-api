package site.javaghost.conolja.common.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication auth) throws AuthenticationException {
    UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getName());
    if (!passwordEncoder.matches((CharSequence) auth.getCredentials(), userDetails.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    return new UsernamePasswordAuthenticationToken(
      userDetails.getUsername(),
      userDetails.getPassword(),
      userDetails.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
