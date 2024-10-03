package site.javaghost.conolja.common.security;

import jakarta.servlet.http.HttpServletResponse;
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
import site.javaghost.conolja.common.security.jwt.JwtProperties;
import site.javaghost.conolja.common.security.jwt.LoginFilter;
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
      // JWT 검증 필터는 UsernamePasswordAuthenticationFilter 이전에 실행
      .addFilterBefore(loginFilter(http), UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(jwtValidationFilter, LoginFilter.class)
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
  LoginFilter loginFilter(HttpSecurity http) throws Exception {
    return new LoginFilter(authenticationManager(http));
  }

  @Bean
  AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
    builder.authenticationProvider(jwtAuthenticationProvider);
    return builder.build();
  }
}
