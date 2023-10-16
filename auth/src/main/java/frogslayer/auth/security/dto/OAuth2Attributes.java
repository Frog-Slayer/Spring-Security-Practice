package frogslayer.auth.security.dto;

import frogslayer.auth.member.entity.Member;
import frogslayer.auth.security.entity.oauth2.GoogleUserInfo;
import frogslayer.auth.security.entity.oauth2.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuth2Attributes {

    private String nameAttributeKey;
    private OAuth2UserInfo oAuth2UserInfo;

    public OAuth2Attributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo){
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuth2Attributes of(SocialType socialType, String nameAttributeName, Map<String, Object> attributes){
        if (socialType == SocialType.google) return ofGoogle(nameAttributeName, attributes);
        return null;
    }

    private static OAuth2Attributes ofGoogle(String nameAttributeKey, Map<String, Object> attributes){
        return OAuth2Attributes.builder()
                .nameAttributeKey(nameAttributeKey)
                .oAuth2UserInfo(new GoogleUserInfo(attributes))
                .build();
    }

    public Member toEntity(OAuth2UserInfo oAuth2UserInfo){
        return Member.builder()
                .email(oAuth2UserInfo.getEmail())
                .build();
    }
}
