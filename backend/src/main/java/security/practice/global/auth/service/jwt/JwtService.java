package security.practice.global.auth.service.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface JwtService {

    String generateAccessToken(String username);
    String generateRefreshToken();

    Optional<String> extractAccessToken(HttpServletRequest request);
    Optional<String> extractRefreshToken(HttpServletRequest request);
    Optional<String> extractName(String accessToken);
    boolean validateToken(String token);

    void setAccessToken(HttpServletResponse response, String accessToken);
    void setRefreshToken(HttpServletResponse response, String refreshToken);
}
