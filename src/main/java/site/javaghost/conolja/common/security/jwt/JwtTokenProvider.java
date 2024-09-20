package site.javaghost.conolja.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static site.javaghost.conolja.common.security.jwt.JwtConfiguration.AUTH_SCHEME;

@Slf4j
@Component
public class JwtTokenProvider {

    // JWT 에서 Claim 추출
    public Claims extractClaims(String headerValue) {
        if (headerValue != null && headerValue.startsWith(AUTH_SCHEME + " ")) {
            String rawToken = getRawToken(headerValue);
            return Jwts.parserBuilder()
                    .setSigningKey(getPrivateKey(JwtConfiguration.SECRET_KEY))
                    .build()
                    .parseClaimsJws(rawToken).getBody();
        }
        log.error("Auth Scheme 혹은 헤더명이 올바르지 않습니다. - {}", headerValue);
        return null;
    }

    private String getRawToken(String headerValue) {
        return headerValue.substring(AUTH_SCHEME.length() + 1); // +1 은 공백
    }

    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(JwtConfiguration.ISSUER)
                // 만료 시간 = 현재 시각 + 유효 기간
                .setExpiration(new Date(System.currentTimeMillis() + JwtConfiguration.ACCESS_TOKEN_EXPIRATION))
                .setIssuedAt(new Date())
                .signWith(getPrivateKey(JwtConfiguration.SECRET_KEY))
                .compact();
    }

    private Key getPrivateKey(String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.RS512.getJcaName());
    }

    public boolean isTokenValid(String token) {
        // 토큰 유효성 검사
        // 1. 토큰의 서명을 확인
        // 2. 만료일자 확인
        // 3. 발급자 확인
        // 4. 토큰의 유형 확인
        // 5. 토큰의 청중 확인

        return true;

    }
}
