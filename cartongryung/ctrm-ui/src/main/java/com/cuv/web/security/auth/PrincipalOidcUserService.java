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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * sns 로그인 (애플) 조회 및 저장
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOidcUserService extends OidcUserService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMemberRepository notificationMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AlimTalkService alimTalkService;

    /**
     * 로그인 > 애플 로그인 > 사용자 정보 저장
     *
     * @param userRequest 애플 로그인 요철
     * @author SungHa
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        oAuth2UserInfo = new AppleUserInfo(oidcUser.getAttributes());

        String providerId = oAuth2UserInfo.getProviderId();
        String loginId = oAuth2UserInfo.getProvider() + "_" + providerId;
        String password = passwordEncoder.encode(loginId + "_" + UUID.randomUUID());
        String email = oAuth2UserInfo.getEmail();

        MemberInfoDto memberNotNormalCount = memberRepository.searchEmailAndProviderIdAndMemberStatus(email, providerId);

        if(memberNotNormalCount != null) {
            switch (memberNotNormalCount.getStatusCode()) {
                case PAUSE -> throw new LockedException("일시" + MemberStatus.PAUSE.getDetail() + "된 계정입니다. 관리자에게 문의하세요.");
//                case SECESSION -> throw new DisabledException(MemberStatus.SECESSION.getDetail() + "한 계정입니다. 관리자에게 문의하세요.");
            }
        }
        Member dbMember = memberRepository.findByEmailAndProviderIdIsNotNullAndStatusCode(email, MemberStatus.NORMAL);
        if (dbMember != null && dbMember.getRegCode() != RegType.APPLE) {
            throw new BadCredentialsException(dbMember.getRegCode().getDetail());
        }

        // SNS 로그인을 통해 최초 로그인한 사용자는 회원가입, 이미 가입된 사용자는 정보 업데이트
        Member member = memberRepository.findByLoginIdAndStatusCode(loginId,MemberStatus.NORMAL);

        if (member == null) {
            member = Member.builder()
                    .providerId(providerId)
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .mobileNumber("01000000000")
                    .regCode(RegType.APPLE)
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

        }

        // HttpServletResponse 객체 가져오기
        HttpServletRequest request = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // 최초 연동이 성공하면 Parameter=user 에 이름 정보가 있음
        // 형식: {"name":{"firstName":"이름","lastName":"성"},"email":"이메일"}
        String userJson = request.getParameter("user");
        String realName = null;
        if (userJson != null)
            try {
                JsonNode userNode = new ObjectMapper().readValue(userJson, new TypeReference<>() {});
                JsonNode nameNode = userNode.get("name");
                realName = nameNode.get("lastName").asText() + nameNode.get("firstName").asText();
            } catch (JsonProcessingException ignored) {}

        if (realName != null && member.getRealName() == null)
            member.setRealName(realName);

        memberRepository.save(member);

        Member finalMember = member;
        OAuth2UserInfo finalOAuth2UserInfo = oAuth2UserInfo;

        return member;

//        return new OidcUser() {
//
//            @Override
//            public Map<String, Object> getClaims() {
//                return null;
//            }
//
//            @Override
//            public OidcUserInfo getUserInfo() {
//                return (OidcUserInfo) finalOAuth2UserInfo;
//            }
//
//            @Override
//            public OidcIdToken getIdToken() {
//                return null;
//            }
//
//            @Override
//            public Map<String, Object> getAttributes() {
//                return finalOAuth2UserInfo.getAttributes();
//            }
//
//            @Override
//            public Collection<? extends GrantedAuthority> getAuthorities() {
//                List<GrantedAuthority> authorities = new ArrayList<>();
//                authorities.add(new SimpleGrantedAuthority(finalMember.getRole().getRole()));
//
//                return authorities;
//            }
//
//            @Override
//            public String getName() {
//                return finalMember.getLoginId();
//            }
//        };
    }

}
