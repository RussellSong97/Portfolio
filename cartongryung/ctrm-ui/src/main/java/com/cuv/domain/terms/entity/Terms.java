package com.cuv.domain.terms.entity;

import com.cuv.common.BaseEntity;
import com.cuv.domain.terms.enumset.TermsType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 서비스 약관 관리
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "terms")
public class Terms extends BaseEntity {

    /** 서비스 약관 관리 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_id")
    private Long id;

    /** 약관 유형 (사용자 이용약관, 개인정보처리방침) */
    @Convert(converter = TermsType.TermsTypeConverter.class)
    @Column(name = "category_code")
    private TermsType termsType;

    /** 약관 내용 */
    private String content;
}
