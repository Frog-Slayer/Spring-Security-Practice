package security.practice.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import security.practice.global.auth.service.jwt.JwtService;
import security.practice.global.auth.token.RefreshTokenAuthenticationToken;

import java.io.IOException;

@Slf4j
public class RefreshTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String REFRESH_URL = "/refresh";
    private final JwtService jwtService;

    public RefreshTokenAuthenticationFilter(JwtService jwtService) {
        super(REFRESH_URL);
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String refreshToken = jwtService.extractRefreshToken(request)
                .orElseThrow(() -> new AuthenticationServiceException("No refresh token provided"));

        log.info("refresh token: {}", refreshToken);

        RefreshTokenAuthenticationToken refreshTokenAuthenticationToken= RefreshTokenAuthenticationToken.unAuthenticated(refreshToken, null);
        return this.getAuthenticationManager().authenticate(refreshTokenAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = authResult.getName();

        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken();

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        jwtService.setAccessToken(response, accessToken);
        jwtService.setRefreshToken(response, refreshToken, username);
    }
}
