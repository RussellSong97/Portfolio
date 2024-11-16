package com.cuv.domain.linkvehicleinfo.entity;

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
 * 카이즈유 차량 정보
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "link_vehicle_info")
public class LinkVehicleInfo extends BaseEntity {

    /** 카이즈유 차량 정보 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_vehicle_info_id")
    private Long id;

    /** 카이즈유코드 */
    private String ciuCode;

    /** 브랜드번호 */
    private Long brandNbr;

    /** 브랜드명 */
    private String brandNm;

    /** 브랜드 영문명 */
    private String brandEngNm;

    /** 제작국가코드 (A:호주, C:중국, E:영국, F:프랑스, G:독일, H: 체코, I:이탈리아, J:일본,
     * K:대한민국, N:네덜란드, P:스페인, Q:퀸즈,R:러시아, S:스웨덴, T:오스트리아, U:미국, Y:옐림) */
    private String prodNationCd;

    /** 국산/수입 여부 (Y:수입, N:국산) */
    private YN importYn;

    /** 브랜드 이미지 URL */
    private String brandImageUrl;

    /** 브랜드 URL(홈페이지) */
    private String brandUrl;

    /** 브랜드 사용 여부 */
    private YN brandActiveYn;

    /** 브랜드 노출 여부 */
    private YN brandExposureYn;

    /** 대표차종번호 */
    private Long repCarClassNbr;

    /** 대표차종명 */
    private String repCarClassNm;

    /** 대표차종 사용 여부 */
    private YN repActiveYn;

    /** 대표차종 노출 여부 */
    private YN repExposureYn;

    /** 차종번호 */
    private Long carClassNbr;

    /** 차종명 */
    private String carClassNm;

    /** 차종 영문명 */
    private String carClassEngNm;

    /** 경쟁차종번호 */
    private String carRivalNbr;

    /** 연형 */
    private String yearType;

    /** 대표이미지 URL */
    private String repImageUrl;

    /** 차종 사용 여부 */
    private YN classActiveYn;

    /** 차종 노출 여부 */
    private YN classExposureYn;

    /** 등급번호 */
    private Long carGradeNbr;

    /** 등급명 */
    private String carGradeNm;

    /** 등급 영문명 */
    private String carGradeNmEng;

    /** 판매구분코드 (1:시판, 2:미정, 3:단종) */
    private Integer salesSeCd;

    /** 판매가격 */
    private Integer salePrice;

    /** 판매가격단위 (0:원, 1:달러, 2:유로, 3:파운드, 4:앤, 5:위안) */
    private Integer salePriceUnit;

    /** 추천여부 */
    private YN recommYn;

    /** 차급 (A: 경형, B:소형, C:준중형, D:중형, E:준대형, F:대형, OTHER:기타) */
    private String carSize;

    /** 외형 (CONVERTIBLE:컨버터블, COUPE:쿠페, HATCHBACK:해치백, LIMOUSINE:리무진, MINIBUS:미니버스, PICKUPTRUCK:픽업트럭, RV:RV,
     * SEDAN:세단, SUV:SUV, TRUCK:트럭, WAGON:왜건) */
    private String extShape;

    /** 변속기 */
    private String istdTrans;

    /** 출시일자 */
    private Integer releaseDt;

    /** 단종일자 */
    private String discontinuedDt;

    /** 차종코드 (1:승용, 2:승합, 3:화물, 4:특수) */
    private Integer carAsortCode;

    /** 사용구분 (01:일반, 02:장애인, 03:택시, 04:렌트형, 05:택시/렌트, 06:기타) */
    private Integer useType;

    /** 출시여부 */
    private YN cmtYn;

    /** 등급 사용 여부 */
    private YN gradeActiveYn;

    /** 등급 노출 여부 */
    private YN gradeExposureYn;

    /** 수정 일자 */
    private LocalDateTime lastUpdateDttm;

    /** 차량 정보 json */
    private String vehicleInfoJson;
}
