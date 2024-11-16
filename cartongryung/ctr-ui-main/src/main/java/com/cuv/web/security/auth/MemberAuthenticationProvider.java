package com.cuv.web.security.auth;

import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.security.auth.login.FailedLoginException;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider {

    private final MemberPrincipalDetailsService memberPrincipalDetailsService;

    /**
     * 인증 처리
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userName = authentication.getName();
        String password = (String) authentication.getCredentials();

        Member member = (Member) memberPrincipalDetailsService.loadUserByUsername(userName);
        if(member == null)
            throw new BadCredentialsException("일치하는 정보가 없습니다.");
        if(!passwordEncoder.matches(password, member.getPassword()))
            throw  new BadCredentialsException("비밀번호가 일치하지 않습니다.");

        switch (member.getStatusCode()) {
            case PAUSE -> throw new LockedException("일시정지 된 계정입니다. 관리자에게 문의하세요.");
            case SECESSION -> throw new DisabledException("탈퇴한 계정입니다. 관리자에게 문의하세요.");
        }


        // 추후 상태값 확인 후 처리 필요
        return new UsernamePasswordAuthenticationToken(member, member.getPassword(), member.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
