package com.cuv.web.security.auth;

import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.member.enumset.RegType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrincipalOauth2UserServiceTest {

    @Autowired
    MemberRepository memberRepository;


    @Test
    void a() {
        memberRepository.save(Member.builder()
                .realName("1a23a4a5a")
                .password("1a2a3a45a")
                .email("1a2a3a4a5a@java.com")
                .mobileNumber("010-0000-0000")
                .regCode(RegType.KAKAO)
                .statusCode(MemberStatus.NORMAL)
                .build());
    }
}