package com.cuv.domain.member.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.member.enumset.RegType;
import com.cuv.web.security.auth.Oauth2UserInfo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.security.auth.Subject;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "member")
public class Member extends BaseEntity implements UserDetails, UserPrincipal, OAuth2User {

    /** 회원 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    /** 가입 코드 (REG: 카카오 | 네이버 | 애플 | 구글 | 이메일) */
    @Convert(converter = RegType.RegTypeConverter.class)
    private RegType regCode;

    /** 이메일 */
    private String email;

    /** 비밀번호 */
    private String password;

    /** 회원 등급 */
    @Convert(converter = MemberRole.MemberRoleConverter.class)
    private MemberRole role;

    /** 상태 코드 (MST : 정상 | 정지 | 탈퇴) */
    @Convert(converter = MemberStatus.MemberStatusConverter.class)
    private MemberStatus statusCode;

    /** 이름 */
    private String realName;

    /** 휴대폰 번호 */
    private String mobileNumber;

    /** 마지막 로그인 일시 */
    private LocalDateTime lastLoginAt;

    /** 탈퇴 사유 */
    private String withdrawReason;

    /** 탈퇴 일시 */
    private LocalDateTime withdrawAt;

    /** 만 14세 이상 여부 */
    private YN yearsOlderYn;

    /** 약관 동의 여부 */
    private YN agreeTermsYn;

    /** 개인정보 처리방침 동의 여부 */
    private YN agreePrivacyYn;

    /** 마케팅 정보 수신 동의 여부 */
    private YN agreeMarketingYn;

    /** 인증 여부 */
    private YN authYn;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.getRole()));

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }


    @Override
    public boolean isEnabled() {
        return MemberStatus.NORMAL == this.statusCode;
    }

    @Override
    public String getName() {
        return this.realName;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }


    @Override
    public boolean implies(Subject subject) {
        return UserPrincipal.super.implies(subject);
    }
}
