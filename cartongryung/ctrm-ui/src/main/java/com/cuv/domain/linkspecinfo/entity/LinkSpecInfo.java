package com.cuv.domain.linkspecinfo.entity;


import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 카이즈유 제원 정보
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "link_spec_info")
public class LinkSpecInfo extends BaseEntity {

    /** 카이즈유 제원 정보 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_spec_info_id")
    private Long id;

    /** 등급번호 */
    private Long carGradeNbr;

    /** 제원 이미지 URL */
    private String specImageId;

    /** 제원 카테고리 (11:구동, 41:엔진, 51:연비, 71:치수) */
    private String specCtgry;

    /** 재원명 */
    private String specNm;

    /** 영숫자 카테고리 (N:Numeric, C:Character) */
    private String alphanumCtgry;

    /** 제원 멀티 아이템 */
    private String specMultiItem;

    /** 제원 단위 */
    private String specUom;

    /** 제원값 */
    private String specValue;

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
