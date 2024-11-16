package com.cuv.web.security.auth;

import com.cuv.common.YN;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.member.enumset.RegType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Member member = null;
        Oauth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if (provider.equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        }

        String providerId = (oAuth2UserInfo).getProviderId();
        String username = provider + "_" + providerId;

        RegType regCode = null;
        if(provider.equals("google")) {
            regCode = RegType.GOOGLE;
        } else if(provider.equals("kakao")) {
            regCode = RegType.KAKAO;
        } else if (provider.equals("naver")) {
            regCode = RegType.NAVER;
        } else {
            regCode = RegType.APPLE;
        }

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = new BCryptPasswordEncoder().encode(uuid);

        //TODO 추후 주석 제거 및 구현
        String email = (oAuth2UserInfo).getEmail();

        System.out.println("email 값 확인 : " + email);

        // 임시
        member = memberRepository.findByEmail(email);

        if (member == null) {
            member = Member.builder()
                    .realName(username)
                    .password(password)
                    .email(email)
                    .mobileNumber("01000000000")
                    .regCode(regCode)
                    .statusCode(MemberStatus.NORMAL)
                    .role(MemberRole.USER)
                    .authYn(YN.N)
                    .build();

            try {
                member = memberRepository.save(member);
            } catch (Exception e) {
                log.error("member save error : {}", e.getMessage());
                throw new RuntimeException();
            }
        }

        return member;
    }
}
