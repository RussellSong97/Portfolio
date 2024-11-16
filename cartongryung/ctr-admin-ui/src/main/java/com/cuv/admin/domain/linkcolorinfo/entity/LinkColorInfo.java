package com.cuv.admin.domain.linkcolorinfo.entity;

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
 * 카이즈유 색상정보
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "link_color_info")
public class LinkColorInfo extends BaseEntity {

    /** 카이즈유 색상 정보 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_color_info_id")
    private Long id;

    /** 등급번호 */
    private Long carGradeNbr;

    /** 외부색상명 */
    private String outClrNm;

    /** 외부 색상 가격 */
    private Integer outClrPrice;

    /** 외부 색상 아이콘 */
    private String outClrIcon;

    /** 색상 이미지 url */
    private String clrImgUrl;

    /** 내장색상명 */
    private String inClrNm;

    /** 내장색상가격 */
    private Integer inClrPrice;

    /** 내장색상아이콘 */
    private String inClrIcon;

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
