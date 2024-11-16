package com.cuv.admin.domain.authoritymenu.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 메뉴 권한
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Table(name = "authority_menu")
public class AuthorityMenu {

    /**
     * 메뉴 권한 일련번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_menu_id")
    private Long id;

    /**
     * URL 패턴
     */
    @Column(name = "url_pattern", nullable = false)
    private String urlPattern;

    /**
     * 설명
     */
    private String description;

    /**
     * 관리 여부 (관리자의 메뉴 권한 노출 여부)
     */
    private String manageYn;

}
