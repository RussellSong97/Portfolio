package com.cuv.domain.member.entity;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.member.enumset.RegType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 회원 정보
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class Member implements UserDetails, OAuth2User, OidcUser {

    /** 회원 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    /** 가입 코드 (REG: 카카오 | 네이버 | 애플 | 구글 | 이메일) */
    @Convert(converter = RegType.RegTypeConverter.class)
    private RegType regCode;

    /** sns 제공 아이디 */
    private String providerId;

    /** 로그인 아이디 */
    private String loginId;

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

    /** 프로필 이미지 JSON */
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto profileImageJson;

    /** 만 14세 이상 여부 */
    private YN yearsOlderYn;

    /** fcm 토큰 */
    private String fcmToken;

    /** 약관 동의 여부 */
    private YN agreeTermsYn;

    /**
     * 공지 알림 여부
     */
    private YN agreeNoticeYn;

    /**
     * 푸시 알림 여부
     */
    private YN agreePushYn;

    /** 개인정보 처리방침 동의 여부 */
    private YN agreePrivacyYn;

    /** 마케팅 정보 수신 동의 여부 */
    private YN agreeMarketingYn;

    /** 인증 여부 */
    private YN authYn;

    @Builder.Default
    @Column(columnDefinition = "CHAR(1)", nullable = false, insertable = false)
    @ColumnDefault("'N'")
    @Comment("삭제 여부")
    protected YN delYn = YN.N;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    @Comment("등록자 ID")
    private String memberCreatedId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @ColumnDefault("NOW(6)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Comment("등록 시간")
    private LocalDateTime memberCreatedAt;

    @LastModifiedBy
    @Column(insertable = false)
    @Comment("수정자 ID")
    private String memberUpdatedId;

    @LastModifiedDate
    @Column(insertable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Comment("수정 시간")
    private LocalDateTime memberUpdatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.getRole()));

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.loginId;
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

//    @Override
//    public <A> A getAttribute(String name) {
//        return OAuth2User.super.getAttribute(name);
//    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Map<String, Object> getClaims() {
        return Map.of();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}
