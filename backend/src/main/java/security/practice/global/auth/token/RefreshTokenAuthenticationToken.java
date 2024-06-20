package security.practice.global.auth.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RefreshTokenAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Object credentials;

    protected RefreshTokenAuthenticationToken(Object principal, Object credentials) {
        super(null);
        setAuthenticated(false);
        this.principal = principal;
        this.credentials = credentials;
    }

    protected RefreshTokenAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(null);
        setAuthenticated(true);
        this.principal = principal;
        this.credentials = credentials;
    }
    public static RefreshTokenAuthenticationToken unAuthenticated(Object principal, Object credentials) {
        return new RefreshTokenAuthenticationToken(principal, credentials);
    }

    public static RefreshTokenAuthenticationToken authenticated(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new RefreshTokenAuthenticationToken(principal, credentials, authorities);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
