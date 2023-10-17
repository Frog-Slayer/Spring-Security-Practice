package frogslayer.auth.security.service.oauth2;

import frogslayer.auth.member.entity.Member;
import frogslayer.auth.member.entity.Role;
import frogslayer.auth.member.repository.MemberRepository;
import frogslayer.auth.security.dto.OAuth2Attributes;
import frogslayer.auth.security.dto.SocialType;
import frogslayer.auth.security.entity.oauth2.CustomOAuth2User;
import frogslayer.auth.security.util.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultUserService = new DefaultOAuth2UserService();

        SocialType socialType = SocialType.valueOf(userRequest.getClientRegistration().getRegistrationId());

        OAuth2User oAuth2User = defaultUserService.loadUser(userRequest);
        String nameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(socialType, nameAttributeName, attributes);

        Member member = getMember(oAuth2Attributes);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
                attributes,
                oAuth2Attributes.getNameAttributeKey(),
                member
        );
    }

    private Member getMember(OAuth2Attributes attributes){
        String email = attributes.getOAuth2UserInfo().getEmail();

        Member findMember = memberRepository.findByEmail(email).orElse(null);

        if (findMember == null) {
            return createUser(attributes);
        }

        return findMember;
    }

    private Member createUser(OAuth2Attributes attributes){
        Member member = attributes.toEntity(attributes.getOAuth2UserInfo());
        member.setRole(Role.GUEST);

        if (member.getPassword() == null){
            member.setPassword(PasswordUtil.generateRandomPassword());
        }

        return memberRepository.save(member);
    }
}

