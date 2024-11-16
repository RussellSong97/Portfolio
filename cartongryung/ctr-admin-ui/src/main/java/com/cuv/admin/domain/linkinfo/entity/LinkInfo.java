package com.cuv.admin.domain.linkinfo.entity;

import com.cuv.admin.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 카이즈유 차량 정보 조회
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "link_info")
public class LinkInfo extends BaseEntity {

    /** 차량 정보 조회 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_info_id")
    private Long id;

    /** 차대 번호 */
    private String vin;

    /** 차량 번호 */
    private String vhrno;

    /** 브랜드번호 */
    private Long brandNbr;

    /** 브랜드명 */
    private String brandNm;

    /** 대표차종번호 */
    private Long repCarClassNbr;

    /** 대표차종명 */
    private String repCarClassNm;

    /** 연형 */
    private String yearType;

    /** 차종번호 */
    private Long carClassNbr;

    /** 차종명 */
    private String carClassNm;

    /** 등급번호 */
    private Long carGradeNbr;

    /** 등급명(표준) */
    private String carGradeNm;

    /** 등급연비 */
    private String gradeFuelRate;

    /** 타이어사이즈_앞 */
    private String tireSizeFront;

    /** 타이어사이즈_뒤 */
    private String tireSizeBack;

    /** 색상 */
    private String colorNm;

    /** 연료 */
    private String fuel;

    /** 배기량 */
    private String enginesize;

    /** 변속기 */
    private String istdTrans;

    /** 외형 */
    private String extShape;

    /** 승차정원 */
    private String person;

    /** 엔진형식 */
    private String engineForm;

    /** 연식 */
    private String prye;

    /** 자동차검사기간 */
    private String insptValidPdDe;

    /** 자동차검사주행거리 */
    private Long trvlDstnc;

    /** 최초등록일 */
    private String frstRegistDe;

    /** 차량 정보 조회 json */
    private String carInfoJson;

}
