package site.javaghost.conolja.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.javaghost.conolja.common.security.jwt.JwtAuthenticationFilter;
import site.javaghost.conolja.common.security.jwt.JwtAuthenticationProvider;
import site.javaghost.conolja.common.security.jwt.JwtProperties;
import site.javaghost.conolja.common.security.jwt.JwtTokenUtil;
import site.javaghost.conolja.common.security.jwt.JwtValidationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  private final JwtValidationFilter jwtValidationFilter;
  private final JwtTokenUtil jwtTokenUtil;
  private final UserDetailsService userDetailsService;
  private final JwtProperties jwtProperties;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/apis", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
          .anyRequest().authenticated()
      )
      .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
    HttpSecurity http,
    AuthenticationProvider authenticationProvider) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
      http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    return authenticationManagerBuilder.build();
  }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
      return new JwtAuthenticationProvider(passwordEncoder, userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
      return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // bcrypt 사용
    }

    @Bean
    public AbstractAuthenticationProcessingFilter jwtAuthenticationFilter(
      AuthenticationManager authenticationManager) throws Exception {
        AbstractAuthenticationProcessingFilter filter = new JwtAuthenticationFilter(jwtTokenUtil, jwtProperties);
      filter.setAuthenticationManager(authenticationManager);
      filter.setFilterProcessesUrl("/api/login");
      return filter;
    }
}
