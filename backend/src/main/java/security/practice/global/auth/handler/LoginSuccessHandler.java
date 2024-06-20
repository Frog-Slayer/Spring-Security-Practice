package security.practice.global.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import security.practice.global.auth.service.jwt.JwtService;

@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String username = authentication.getName();
        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken();

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        log.info("access token: {}", accessToken);
        log.info("refresh token: {}", refreshToken);

        jwtService.setAccessToken(response, accessToken);
        jwtService.setRefreshToken(response, refreshToken, username);
    }
}
