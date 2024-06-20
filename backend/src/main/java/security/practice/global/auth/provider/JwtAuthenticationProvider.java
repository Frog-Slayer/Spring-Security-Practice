package security.practice.global.auth.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.ClassUtils;
import security.practice.global.auth.service.jwt.JwtService;
import security.practice.global.auth.token.JwtAuthenticationToken;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = authentication.getPrincipal().toString();
        UserDetails userDetails = retrieveUser(accessToken);
        return JwtAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
    }

    protected UserDetails retrieveUser(String accessToken) throws AuthenticationException {
        return jwtService.extractName(accessToken)
                .map(userDetailsService::loadUserByUsername)
                .orElseThrow(()-> new AuthenticationServiceException("Could not extract username from JWT token"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClassUtils.isAssignable(JwtAuthenticationToken.class, authentication);
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

}
