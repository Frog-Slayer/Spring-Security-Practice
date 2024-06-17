package security.practice.global.auth.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService{

    public static final String BEARER = "Bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final SecretKey secretKey;

    public JwtServiceImpl(@Value("${jwt.secret-key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    @Override
    public String generateAccessToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .claim("sub", username)
                .signWith(secretKey)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessTokenExpiration))
                .compact();
    }

    @Override
    public String generateRefreshToken() {
        return "";
    }

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.empty();
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.empty();
    }

    @Override
    public Optional<String> extractName(String accessToken) {
        return Optional.empty();
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }

    @Override
    public void setAccessToken(HttpServletResponse response, String accessToken) {

    }

    @Override
    public void setRefreshToken(HttpServletResponse response, String refreshToken) {

    }
}
