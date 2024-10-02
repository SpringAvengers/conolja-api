package site.javaghost.conolja.common.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.javaghost.conolja.common.security.jwt.JwtAuthenticationFilter;
import site.javaghost.conolja.common.security.jwt.JwtAuthenticationProvider;
import site.javaghost.conolja.common.security.jwt.JwtProperties;
import site.javaghost.conolja.common.security.jwt.JwtTokenUtil;
import site.javaghost.conolja.common.security.jwt.JwtValidationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtValidationFilter jwtValidationFilter;
  private final UserDetailsService userDetailsService;
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
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()  // static resources 허용
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/apis").permitAll()
                    .requestMatchers("/swagger-ui/**", "/api-docs/**", "/api/swagger-ui.html").permitAll()
                    .anyRequest().authenticated()  // 나머지 경로는 인증 요구
            )

            // JWT 검증 필터는 UsernamePasswordAuthenticationFilter 이전에 실행

            .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
            // JWT 인증 필터는 UsernamePasswordAuthenticationFilter 와 동일한 위치에서 실행
            .addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authenticationProvider(jwtAuthenticationProvider(passwordEncoder(), userDetailsService))
            //커스텀 에러 핸들링
            .exceptionHandling(exception -> exception
                    // 권한이 없을 때 (403) 커스텀 처리
                    .accessDeniedHandler((request, response, accessDeniedException) -> { // 인가 핸들러
                      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                      response.setCharacterEncoding("UTF-8");
                      response.setContentType("text/plain; charset=UTF-8");
                      response.getWriter().write("권한이 없는 사용자 : 403 Error.");
                    })
                    // 인증되지 않은 사용자가 접근할 때 (401)
                    .authenticationEntryPoint((request, response, authException) -> { // 인증 핸들러
                      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                      response.setCharacterEncoding("UTF-8");
                      response.setContentType("text/plain; charset=UTF-8");
                      response.getWriter().write("인증되지 않은 사용자 : 401 Error.");
                    })
            );

    return http.build();
  }

  @Bean
  JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtTokenUtil, jwtProperties);
    filter.setAuthenticationManager(authenticationManager());
    return filter;
  }

  @Bean
  AuthenticationManager authenticationManager() throws Exception {
    return new ProviderManager(jwtAuthenticationProvider(passwordEncoder(), userDetailsService));
  }

  @Bean
  public AuthenticationProvider jwtAuthenticationProvider(PasswordEncoder encoder, UserDetailsService service) {
    return new JwtAuthenticationProvider(encoder, service);
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // bcrypt 사용
  }
}
