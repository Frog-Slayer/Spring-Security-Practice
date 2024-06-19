package security.practice.global.auth.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PreJwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String principal;
    private final Object credentials;

    public PreJwtAuthenticationToken(String principal, String credentials) {
        super(null);
        setAuthenticated(false);
        this.principal = principal;
        this.credentials = credentials;
    }

    public PreJwtAuthenticationToken(String principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
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
    public String getPrincipal() {
        return this.principal;
    }
}
