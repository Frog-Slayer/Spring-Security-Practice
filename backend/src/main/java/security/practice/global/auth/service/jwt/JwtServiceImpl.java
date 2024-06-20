package security.practice.global.auth.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import security.practice.global.auth.utils.RedisUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService{

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "Refresh";

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final SecretKey secretKey;
    private final RedisUtils redisUtils;

    public JwtServiceImpl(@Value("${jwt.secret-key}") String secretKey, RedisUtils redisUtils) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.redisUtils = redisUtils;
    }

    @Override
    public String generateAccessToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)
                .signWith(secretKey, Jwts.SIG.HS512)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessTokenExpiration))
                .compact();
    }

    @Override
    public String generateRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .claim("sub", UUID.randomUUID().toString())
                .signWith(secretKey, Jwts.SIG.HS512)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTokenExpiration))
                .compact();
    }

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        log.info("extractRefreshToken");
        return Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(e -> e.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
                        .findAny())
                .map(Cookie::getValue);
    }

    @Override
    public Optional<String> extractName(String accessToken) {
        try {
            Jws<Claims> claims = validateToken(accessToken);
            return Optional.of(claims.getPayload().getSubject());
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Jws<Claims> validateToken(String token) throws Exception {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    @Override
    public void setAccessToken(HttpServletResponse response, String accessToken) {
        accessToken = BEARER + accessToken;
        response.setHeader(AUTHORIZATION_HEADER, accessToken);
    }

    @Override
    public void setRefreshToken(HttpServletResponse response, String refreshToken, String username) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .maxAge(-1)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        saveRefreshToken(refreshToken, username);
    }

    private void saveRefreshToken(String refreshToken, String username) {
        redisUtils.set(refreshToken, username, refreshTokenExpiration);
    }

    @Override
    public void removeRefreshToken(String refreshToken){
        redisUtils.delete(refreshToken);
    }

    @Override
    public String getUsernameByRefreshToken(String refreshToken) throws Exception {
        validateToken(refreshToken);
        return redisUtils.get(refreshToken);
    }

}
