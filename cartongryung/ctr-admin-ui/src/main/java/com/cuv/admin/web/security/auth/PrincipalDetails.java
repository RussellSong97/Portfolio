package com.cuv.admin.web.security.auth;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

/**
 * 관리자 회원 정보
 */
@Getter
public class PrincipalDetails implements UserDetails {

    private final MemberAdmin user;

    private final List<String> allowUrlPatterns = new ArrayList<>();

    public PrincipalDetails(MemberAdmin user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return YN.N == user.getDelYn();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return YN.Y == user.getUseYn();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        if (user == null || !hasText(user.getRole().getRole()))
            return collection;

        collection.add(() -> user.getRole().getRole());

        return collection;
    }
}
