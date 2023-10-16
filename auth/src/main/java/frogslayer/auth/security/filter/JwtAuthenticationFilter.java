package frogslayer.auth.security.filter;

import frogslayer.auth.member.entity.Member;
import frogslayer.auth.member.repository.MemberRepository;
import frogslayer.auth.security.entity.CustomUserDetails;
import frogslayer.auth.security.repository.RefreshTokenRepository;
import frogslayer.auth.security.service.JwtService;
import frogslayer.auth.security.util.PasswordUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String LOGIN_URL = "/api/login";
    private static final String REFRESH_URL = "/api/refresh";

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals(LOGIN_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!request.getRequestURI().equals(REFRESH_URL)){
            authenticateWithAccessToken(request, response, filterChain);
            return;
        }

        reissueTokens(request, response);
    }

    public void authenticateWithAccessToken(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {

        jwtService.extractAccessToken(request)
                .filter(jwtService::isAccessTokenValid)
                .ifPresent(accessToken -> jwtService.extractNameFromToken(accessToken)
                        .ifPresent(email-> memberRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    public void reissueTokens(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isRefreshTokenValid)
                .orElse(null);

        if (refreshToken == null) return;

        Long memberId = refreshTokenRepository.findById(refreshToken).get().getMemberId();

        memberRepository.findById(memberId)
                .ifPresent(member-> {
                    String reIssuedRefreshToken = jwtService.issueRefreshToken();
                    jwtService.sendBothTokens(response,
                            jwtService.issueAccessToken(member.getEmail()),
                            reIssuedRefreshToken);
                    jwtService.saveRefreshToken(member.getEmail(), reIssuedRefreshToken);
                });
    }

    public void saveAuthentication(Member member) {

        String password = member.getPassword();

        if (password == null) {
            password = PasswordUtil.generateRandomPassword();
            member.setPassword(password);
        }

        CustomUserDetails principalDetails = new CustomUserDetails(member);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principalDetails, null,
                        authoritiesMapper.mapAuthorities(principalDetails.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
