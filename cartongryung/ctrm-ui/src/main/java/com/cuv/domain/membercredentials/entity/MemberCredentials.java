package com.cuv.domain.membercredentials.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.membercredentials.enumset.CredentialsType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원 인증 정보
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "member_credentials")
public class MemberCredentials extends BaseEntity {
    /** 인증 번호 테이블 시퀀스 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_credentials_id")
    private Long id;

    /** 회원 시퀀스 */
    @Column(name = "member_id")
    private Long memberId;

    /** 인증 타입 */
    @Column(name= "type")
    @Convert(converter = CredentialsType.CredentialsTypeConverter.class)
    private CredentialsType type;

    /** 전화 번호 */
    @Column(name="phone_number")
    private String phoneNumber;

    /** 인증 토큰 */
    @Column(name="auth_token")
    private String authToken;

    /** 인증 번호 */
    @Column(name="auth_number")
    private String authNumber;

    /** 만료 시간 */
    @Column(name="expired_at")
    private LocalDateTime expiredAt;

    /** 사용 여부 */
    @Column(name= "use_yn")
    private YN useYn;
}
