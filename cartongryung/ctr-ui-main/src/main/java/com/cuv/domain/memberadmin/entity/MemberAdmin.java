package com.cuv.domain.memberadmin.entity;


import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.member.enumset.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Table(name = "member_admin")
public class MemberAdmin extends BaseEntity {

    /** 관리자 회원 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_admin_id")
    private Long id;

    /** 아이디 */
    private String loginId;

    /** 비밀번호 */
    private String password;

    /** 회원 등급 (ROLE: 딜러, 상담사, 관리자) */
    @Convert(converter = MemberRole.MemberRoleConverter.class)
    private MemberRole role;

    /** 이름 */
    private String realName;

    /** 휴대폰 번호 */
    private String mobileNumber;

    /** 사원증 번호 */
    private String employeeNumber;

    /** 한줄 소개 */
    private String intro;

    /** 프로필 이미지 JSON */
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto profileImageJson;

    /** 마지막 로그인 일시 */
    private LocalDateTime lastLoginAt;

    /** 사용 여부 */
    private YN useYn;

}
