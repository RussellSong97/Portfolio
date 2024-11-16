package com.cuv.admin.web.security.auth;

import com.cuv.admin.domain.authoritymenu.AuthorityMenuRepository;
import com.cuv.admin.domain.authoritymenu.entity.AuthorityMenu;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.memberadmin.MemberAdminRepository;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * 시큐리티 로그인 조회
 */
@Service
@RequiredArgsConstructor
public class AdminPrincipalDetailService implements UserDetailsService {

    private final MemberAdminRepository memberAdminRepository;

    private final AuthorityMenuRepository authorityMenuRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberAdmin memberAdmin = memberAdminRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지 않습니다."));

        PrincipalDetails principalDetails = new PrincipalDetails(memberAdmin);
        Collection<? extends GrantedAuthority> authorities = principalDetails.getAuthorities();

        List<String> hasRoles = authorities.stream().map(GrantedAuthority::getAuthority).toList();
        List<String> allowUrlPatterns = principalDetails.getAllowUrlPatterns();

        // 관리자면 모든 권한 추가
        if (MemberRole.ADMIN.getRole().equals(hasRoles.get(0))) {
            allowUrlPatterns.addAll(
                    Stream.of(authorityMenuRepository.findAll())
                            .flatMap(List::stream)
                            .map(AuthorityMenu::getUrlPattern)
                            .distinct()
                            .toList());
        } else {
            // 해당 회원에 맞는 권한 URL 가져오기
            allowUrlPatterns.addAll(
                    Stream.of(authorityMenuRepository.findAllAuthorityMenuByLoginId(memberAdmin.getLoginId()))
                            .flatMap(List::stream)
                            .map(AuthorityMenu::getUrlPattern)
                            .distinct()
                            .toList());
        }

        return principalDetails;
    }

    /**
     * 관리자 | 로그인 시 마지막 로그인 일시 업데이트
     *
     * @author SungHa
     */
    public void updateLastLoginAt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof PrincipalDetails principalDetails))
            return;

        MemberAdmin memberAdmin = principalDetails.getUser();
        memberAdmin.setLastLoginAt(LocalDateTime.now());
        memberAdminRepository.save(memberAdmin);
    }
}
