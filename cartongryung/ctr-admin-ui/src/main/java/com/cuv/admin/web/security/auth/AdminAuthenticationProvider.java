package com.cuv.admin.web.security.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 인증 처리
 */
@Component
@RequiredArgsConstructor
public class AdminAuthenticationProvider implements AuthenticationProvider {

    @Getter
    private final AdminPrincipalDetailService adminPrincipalDetailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        PrincipalDetails principalDetails = (PrincipalDetails) adminPrincipalDetailService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, principalDetails.getPassword()))
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");

        if (!principalDetails.isAccountNonExpired())
            throw new AccountExpiredException("만료된 계정입니다. 관리자에게 문의하세요.");

        if (!principalDetails.isEnabled())
            throw new DisabledException("계정이 비활성화 되었습니다. 관리자에게 문의하세요.");

        if (!principalDetails.isAccountNonLocked())
            throw new LockedException("계정이 잠겼습니다. 관리자에게 문의하세요.");

        if (!principalDetails.isCredentialsNonExpired())
            throw new CredentialsExpiredException("비밀번호가 만료되었습니다. 비밀번호를 변경하고 로그인 해주세요.");

        return new UsernamePasswordAuthenticationToken(
                principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
