package security.practice.global.auth.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetails principal;
    private final Object credentials;

    public JwtAuthenticationToken(UserDetails principal, String credentials) {
        super(null);
        setAuthenticated(false);
        this.principal = principal;
        this.credentials = credentials;
    }

    public JwtAuthenticationToken(UserDetails principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setAuthenticated(true);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public UserDetails getPrincipal() {
        return this.principal;
    }
}
