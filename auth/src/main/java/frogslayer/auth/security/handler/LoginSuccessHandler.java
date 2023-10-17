package frogslayer.auth.security.handler;

import frogslayer.auth.security.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String username = extractUsername(authentication);
        String accessToken = jwtService.issueAccessToken(username);
        String refreshToken = jwtService.issueRefreshToken();

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        jwtService.sendBothTokens(response, accessToken, refreshToken);
        jwtService.saveRefreshToken(username, refreshToken);
    }

    private String extractUsername(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }


}
