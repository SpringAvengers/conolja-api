package site.javaghost.conolja.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import site.javaghost.conolja.common.security.auth.LoginRequest;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;
    private final AuthenticationProvider authenticationProvider;

    public String parseToken(String headerValue) {
        if (null == headerValue) {
            throw new AuthenticationServiceException(jwtProperties.header() + " 헤더가 존재하지 않습니다");
        }

        if(StringUtils.hasText(jwtProperties.header()) && headerValue.startsWith(jwtProperties.prefix())) {
            return headerValue.substring(jwtProperties.prefix().length() + 1); // +1 은 공백
        }
        return null;
    }

    public Authentication getAuthentication (String accessToken) {
        try {
            String username = extractUsername(accessToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 토큰 생성 및 반환
            return new UsernamePasswordAuthenticationToken(
              userDetails,
              userDetails.getPassword(),
              userDetails.getAuthorities());
            
        } catch (UsernameNotFoundException e) {
            log.error(String.valueOf(e));
            throw new BadCredentialsException("사용자를 찾을 수 없습니다.");
        }
    }
    
    public Authentication generateToken (String accessToken) {
        try {
            String username = extractUsername(accessToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // 토큰 생성 및 반환
            return new UsernamePasswordAuthenticationToken(
              userDetails,
              userDetails.getPassword(),
              userDetails.getAuthorities());
            
        } catch (UsernameNotFoundException e) {
            log.error(String.valueOf(e));
            throw new BadCredentialsException("사용자를 찾을 수 없습니다.");
        }
    }

    // JWT 에서 Claim 추출
    private String extractUsername(String accessToken) {
        return getClaims(accessToken).getBody().getSubject();
    }

    public JwtTokenDto generateToken(LoginRequest request){
        String username = request.username();
        String password = request.password();
        Authentication token = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        return generateToken(token);
    }

    // Jwt 토큰 생성
    public JwtTokenDto generateToken(Authentication authentication) {
        // 사용자 정보 추출
        String username = authentication.getName();
        String authority = authentication.getAuthorities().stream().findFirst().get().getAuthority();

        // 액세스 토큰 생성
        String accessToken = Jwts.builder()
          .setSubject(username)
          .setIssuer(jwtProperties.issuer())
          // 만료 시간 = 현재 시각 + 유효 기간
          .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.expiration().toMillis()))
          .setIssuedAt(new Date())
          .signWith(getPrivateKey(jwtProperties.secret()))
          .compact();

        // 리프레시 토큰 생성
        String refreshToken = Jwts.builder()
          .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.refresh().expiration().toMillis()))
          .signWith(getPrivateKey(jwtProperties.secret()))
          .compact();

        return JwtTokenDto.by(accessToken, refreshToken, authority);
    }

    private Key getPrivateKey(String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.RS512.getJcaName());
    }

    public boolean isTokenValid(String token) {
        try {
            return !hasExpired(getClaims(token));
        }catch(Exception e) {
            return false;
        }
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder()
          .setSigningKey(getPrivateKey(jwtProperties.secret()))
          .build()
          .parseClaimsJws(token);
    }

    private boolean hasExpired(Jws<Claims> claims) {
        return !claims.getBody().getExpiration().before(new Date());
    }
}

