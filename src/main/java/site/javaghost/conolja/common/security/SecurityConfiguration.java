package site.javaghost.conolja.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.javaghost.conolja.common.security.jwt.JwtAuthenticationFilter;
import site.javaghost.conolja.common.security.jwt.JwtProperties;
import site.javaghost.conolja.common.security.jwt.JwtTokenUtil;
import site.javaghost.conolja.common.security.jwt.JwtValidationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtValidationFilter jwtValidationFilter;
  private final AuthenticationProvider jwtAuthenticationProvider;
  private final JwtProperties jwtProperties;
  private final JwtTokenUtil jwtTokenUtil;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .csrf(AbstractHttpConfigurer::disable)
      .cors(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth
        // context-path prefix ("/api") 는 붙일 필요 없음 -> spring 이 자동으로 인식함
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()  // static resources 허용
        .requestMatchers("/auth/**").permitAll() // 인증 관련 엔드포인트
        .requestMatchers("/apis", "/swagger-ui/**","/api-docs/**").permitAll() // 스웨거
        .requestMatchers("/error/**").permitAll() // 에러 페이지
        .anyRequest().authenticated()  // 나머지 경로는 인증 요구
      )
      // JWT 검증 필터는 UsernamePasswordAuthenticationFilter 이전에 실행
      .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
      .addFilterAt(jwtAuthenticationFilter(http), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  JwtAuthenticationFilter jwtAuthenticationFilter(HttpSecurity http) throws Exception {
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtTokenUtil, jwtProperties);
    filter.setAuthenticationManager(authenticationManager(http));
    return filter;
  }

  @Bean
  AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
    builder.authenticationProvider(jwtAuthenticationProvider);
    return builder.build();
  }
}
