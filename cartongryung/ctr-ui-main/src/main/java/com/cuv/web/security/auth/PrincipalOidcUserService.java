package com.cuv.web.security.auth;

import com.cuv.common.YN;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.member.enumset.RegType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class PrincipalOidcUserService extends OidcUserService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("load User Call");
        OidcUser user = super.loadUser(userRequest);
        Map<String, Object> attributes;
        Member member = null;

        String idToken = userRequest.getAdditionalParameters().get("id_token").toString();
        attributes = decodeJwtTokenPayload(idToken);
        attributes.put("id_token", idToken);

//        Map<String, Object> userAttributes = new HashMap<>();
//        userAttributes.put("resultcode", "00");
//        userAttributes.put("message", "success");
//        userAttributes.put("response", attributes);
//
//        user = new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), userAttributes, "response");

        Map<String, Object> appleAttributes = user.getAttributes();
        Map<String, Object> name = (Map<String, Object>) appleAttributes.get("name");
        String firstName = name != null ? (String) name.get("firstName") : null;
        String lastName = name != null ? (String) name.get("lastName") : null;
        String username = lastName + firstName;
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = new BCryptPasswordEncoder().encode(uuid);

        String email = user.getAttribute("email");

        // 임시
        member = memberRepository.findByEmail(email);

        if (member == null) {
            member = Member.builder()
                    .realName(username)
                    .password(password)
                    .email(email)
                    .mobileNumber("01000000000")
                    .regCode(RegType.APPLE)
                    .statusCode(MemberStatus.NORMAL)
                    .role(MemberRole.USER)
                    .authYn(YN.N)
                    .build();

            try {
                memberRepository.save(member);
            } catch (Exception e) {
                log.error("member save error : {}", e.getMessage());
                throw new RuntimeException();
            }
        }

        return user;

    }

    public Map<String, Object> decodeJwtTokenPayload(String jwtToken) {
        Map<String, Object> jwtClaims = new HashMap<>();
        try {
            String[] parts = jwtToken.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            byte[] decodedBytes = decoder.decode(parts[1].getBytes(StandardCharsets.UTF_8));
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> map = mapper.readValue(decodedString, Map.class);
            jwtClaims.putAll(map);

        } catch (JsonProcessingException e) {
            log.error("decodeJwtToken: {}-{} / jwtToken : {}", e.getMessage(), e.getCause(), jwtToken);
        }
        return jwtClaims;
    }
}
