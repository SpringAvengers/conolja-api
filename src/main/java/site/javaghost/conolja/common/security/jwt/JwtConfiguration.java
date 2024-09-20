package site.javaghost.conolja.common.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {
    @Value("${jwt.secret}")
    static String SECRET_KEY;
    @Value("${jwt.prefix}")
    static String AUTH_SCHEME;
    @Value("${jwt.header}")
    static String ACCESS_TOKEN_HEADER;
    @Value("${jwt.issuer}")
    static String ISSUER;
    @Value("${jwt.type}")
    static String TYPE;
    @Value("${jwt.expiration}")
    static long ACCESS_TOKEN_EXPIRATION;
    @Value("${jwt.refresh.expiration}")
    static long REFRESH_TOKEN_EXPIRATION;
    @Value("${jwt.refresh.header}")
    static String REFRESH_TOKEN_HEADER;
}
