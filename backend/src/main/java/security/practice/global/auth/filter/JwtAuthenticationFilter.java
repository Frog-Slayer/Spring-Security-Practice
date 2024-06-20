package security.practice.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import security.practice.global.auth.service.jwt.JwtService;
import security.practice.global.auth.token.JwtAuthenticationToken;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(RequestMatcher requestMatcher, JwtService jwtService) {
        super(requestMatcher);
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new AuthenticationServiceException("No access token"));

        Authentication preAuthentication = JwtAuthenticationToken.unAuthenticated(accessToken, null);
        return this.getAuthenticationManager().authenticate(preAuthentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authResult;
        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
        chain.doFilter(request, response);
    }
}
