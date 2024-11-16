package com.cuv.admin.domain.authoritymenuemployee.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 관리자 회원별 메뉴권한
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Table(name = "authority_menu_employee")
public class AuthorityMenuEmployee {

    /** 회원별 메뉴 권한 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_menu_employee_id", nullable = false)
    private Long id;

    /** 관리자 ID */
    private String loginId;

    /** 메뉴 권한 일련번호 */
    private Long authorityMenuId;

}
