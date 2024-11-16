package com.cuv.admin.domain.memberadminmemo.entity;

import com.cuv.admin.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 관리자 회원 메모 정보
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Table(name = "member_admin_memo")
public class MemberAdminMemo extends BaseEntity {
    /** 관리자 회원 메모 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_admin_memo_id")
    private Long id;

    /** 관리자 회원 일련번호 */
    @Column(name = "member_admin_id")
    private Long memberAdminId;

    /** 회원 일련번호 */
    @Column(name = "member_id")
    private Long memberId;

    /** 메모 */
    @Column(name="content")
    private String content;
}
