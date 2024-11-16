package com.cuv.admin.domain.linkoptioninfo.entity;

import com.cuv.admin.common.BaseEntity;
import com.cuv.admin.common.YN;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 카이즈유 옵션 정보
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "link_option_info")
public class LinkOptionInfo extends BaseEntity {

    /** 카이즈유 옵션 정보 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_option_info_id")
    private Long id;

    /** 등급번호 */
    private Long carGradeNbr;

    /** 옵션 구분 (1:무료 옵션, 2:유료 옵션) */
    private Integer optType;

    /** 옵션 선택 구분 (N:미적용, Y:기본, S:선택, Q:알수없음) */
    private String optPickType;

    /** 옵션 카테고리 (21:내장, 31:안전/성능, 61:외장, 81:편의/기능) */
    private String optCtgry;

    /** 옵션명 */
    private String optNm;

    /** 선택 옵션 가격 */
    private String pickOptPrice;

    /** 선택옵션 상세 */
    private String pickOptDesc;

    /** 선택 옵션 그룹 코드 */
    private String pickOptGrpCd;

    /** 브랜드 사용 여부 */
    private YN brandActiveYn;

    /** 브랜드 노출 여부 */
    private YN brandExposureYn;

    /** 대표차종 사용 여부 */
    private YN repActiveYn;

    /** 대표차종 노출 여부 */
    private YN repExposureYn;

    /** 차종 사용 여부 */
    private YN classActiveYn;

    /** 차종 노출 여부 */
    private YN classExposureYn;

    /** 등급 사용 여부 */
    private YN gradeActiveYn;

    /** 등급 노출 여부 */
    private YN gradeExposureYn;

    /** 판매구분코드 (1:시판, 2:미정, 3:단종) */
    private Integer salesSeCd;

    /** 출시여부 */
    private YN cmtYn;

    /** 수정 일자 */
    private LocalDateTime lastUpdateDttm;

}
