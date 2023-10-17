package frogslayer.auth.security.entity.oauth2;

import frogslayer.auth.member.entity.Member;
import frogslayer.auth.member.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {
    private final Member member;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, Member member) {
        super(authorities, attributes, nameAttributeKey);
        this.member = member;
    }

    public Role getRole(){
        return member.getRole();
    }

    @Override
    public String getName() {
        return member.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return super.getAttributes();
    }

    @Override
    public <A> A getAttribute(String name) {
        return super.getAttribute(name);
    }

    public Member getMember() {
        return member;
    }
}
