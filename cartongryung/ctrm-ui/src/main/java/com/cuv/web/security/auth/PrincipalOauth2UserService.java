package com.cuv.web.security.auth;

import com.cuv.common.YN;
import com.cuv.domain.bizgo.AlimTalkService;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.dto.MemberInfoDto;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.member.enumset.RegType;
import com.cuv.domain.notification.NotificationRepository;
import com.cuv.domain.notification.entity.Notification;
import com.cuv.domain.notificationMember.NotificationMemberRepository;
import com.cuv.domain.notificationMember.entity.NotificationMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * SNS 회원 가입 조회 및 저장
 */
@Service
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationMemberRepository notificationMemberRepository;
    @Autowired
    private AlimTalkService alimTalkService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Member member = null;
        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if (provider.equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (provider.equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        } else if (provider.equals("apple")) {
            oAuth2UserInfo = new AppleUserInfo(oAuth2User.getAttributes());
        }

        String providerId = (oAuth2UserInfo).getProviderId();
        String loginId = provider + "_" + providerId;

        RegType regCode = null;
        if(provider.equals("google")) {
            regCode = RegType.GOOGLE;
        } else if(provider.equals("kakao")) {
            regCode = RegType.KAKAO;
        } else if(provider.equals("apple")) {
            regCode = RegType.APPLE;
        } else  {
            regCode = RegType.NAVER;
        }

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = new BCryptPasswordEncoder().encode(uuid);

        String email = (oAuth2UserInfo).getEmail();

        MemberInfoDto memberNotNormalCount = memberRepository.searchEmailAndProviderIdAndMemberStatus(email, providerId);

        if(memberNotNormalCount != null) {
            switch (memberNotNormalCount.getStatusCode()) {
                case PAUSE -> throw new LockedException("일시" + MemberStatus.PAUSE.getDetail() + "된 계정입니다. 관리자에게 문의하세요.");
//                case SECESSION -> throw new DisabledException(MemberStatus.SECESSION.getDetail() + "한 계정입니다. 관리자에게 문의하세요.");
            }
        }

        Member dbMember = memberRepository.findByEmailAndProviderIdIsNotNullAndStatusCode(email, MemberStatus.NORMAL);

        // sns 중복 회원 가입 막기
        if (dbMember != null && dbMember.getRegCode() != regCode) {
            throw new BadCredentialsException(dbMember.getRegCode().getDetail());
        }

        member = memberRepository.findByLoginIdAndStatusCode(loginId,MemberStatus.NORMAL);


        if (member == null) {
            member = Member.builder()
                    .providerId(providerId)
                    .loginId(loginId)
                    .password(password)
                    .realName(oAuth2UserInfo.getName())
                    .email(email)
                    .mobileNumber("01000000000")
                    .regCode(regCode)
                    .statusCode(MemberStatus.NORMAL)
                    .role(MemberRole.USER)
                    .authYn(YN.N)
                    .build();

            memberRepository.save(member);

            String formattedDate = LocalDate.now().toString();

            Notification notification = Notification.builder()
                    .memberId(member.getId())
                    .target("personal")
                    .pushStatus("send")
                    .sendStatus("right")
                    .title("회원가입")
                    .content("안녕하세요. 카통령입니다. \n" +
                            "카통령에 가입해주셔서 감사합니다. \n" +
                            "\n" +
                            "신뢰하실 수 있는 서비스를 약속드립니다.\n" +
                            "\n" +
                            "가입날짜 : " + formattedDate)
                    .readYn(YN.N)
                    .sendAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);

            NotificationMember notificationMember = NotificationMember.builder()
                    .memberId(member.getId())
                    .notificationId(notification.getId())
                    .readYn(YN.N)
                    .build();

            notificationMemberRepository.save(notificationMember);

            // 회원가입 성공 시 알림톡 전송
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("#{day}", formattedDate); // 가입 날짜를 현재 날짜로 설정

            alimTalkService.sendAlimTalk("code01", placeholders, member.getMobileNumber());

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
