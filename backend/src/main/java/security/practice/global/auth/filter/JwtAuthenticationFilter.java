package security.practice.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import security.practice.domain.member.entity.Member;
import security.practice.domain.member.repository.MemberRepository;
import security.practice.global.auth.entity.CustomUserDetails;
import security.practice.global.auth.service.jwt.JwtService;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        if (requestUri.equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        authenticateAccessToken(request);
        filterChain.doFilter(request, response);
    }

    private void authenticateAccessToken(HttpServletRequest request) {
        jwtService.extractAccessToken(request)
                .flatMap(jwtService::extractName)
                .flatMap(memberRepository::findByUsername)
                .ifPresent(this::saveAuthentication);
    }

    private void saveAuthentication(Member member) {
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        Authentication authentication
                = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
