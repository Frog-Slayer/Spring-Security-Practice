package frogslayer.auth.security.handler.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import frogslayer.auth.member.entity.Role;
import frogslayer.auth.member.repository.MemberRepository;
import frogslayer.auth.security.entity.oauth2.CustomOAuth2User;
import frogslayer.auth.security.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MemberRepository memberRepository;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            loginSuccess(request, response, oAuth2User);
        } catch (Exception e){
            log.info("onAuthenticationSuccess: {}", e.getMessage());
        }
    }

    private void loginSuccess(HttpServletRequest request, HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException{
        if (response.isCommitted()) return;

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String accessToken = jwtService.issueAccessToken(oAuth2User.getName());
        String refreshToken = jwtService.issueRefreshToken();

        String redirectUrl = UriComponentsBuilder.fromUriString("/oauth2/redirect/" + accessToken)
                .build().toUriString();

        if (oAuth2User.getRole() == Role.GUEST) {
            redirectUrl = UriComponentsBuilder.fromUriString("/oauth2/signup/"+ accessToken)
                    .build().toUriString();
        }

        log.info("새 로그인 유저 : {}, with role {}", oAuth2User.getName(), refreshToken, oAuth2User.getRole());

        jwtService.sendBothTokens(response, accessToken, refreshToken);
        jwtService.saveRefreshToken(oAuth2User.getName(), refreshToken);

        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }

}
