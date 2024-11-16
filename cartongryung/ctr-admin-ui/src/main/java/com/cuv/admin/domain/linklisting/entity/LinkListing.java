package com.cuv.admin.domain.linklisting.entity;

import com.cuv.admin.common.BaseEntity;
import com.cuv.admin.common.YN;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * 카매니저 매물 연동
 */
@Entity
@DynamicUpdate
@DynamicInsert
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "link_listing")
public class LinkListing extends BaseEntity {

    /** 차량 관리 번호 */
    @Id
    @Column(name = "car_no")
    private Long id;

    /** 제시번호 */
    private Long carJesiNo;

    /** 상사 번호 */
    private Long shopNo;

    /** 상사 이름 */
    private String shopName;

    /** 딜러명 */
    private String userName;

    /** 딜러 휴대폰 번호 */
    private String userHp;

    /** 차량 번호 */
    private String carPlateNumber;

    /** 차량 이름 */
    private String carName;

    /** 연식 */
    private String carRegYear;

    /** 차대번호 */
    private String carFrameNo;

    /** 차량 최초 등록일 */
    private String carRegDay;

    /** 제조사 번호 */
    private Long carMakerNo;

    /** 모델 번호 */
    private Long carModelNo;

    /** 상세 모델 번호 */
    private Long carModelDetailNo;

    /** 등급 번호 */
    private Long carGradeNo;

    /** 상세 등급 번호 */
    private Long carGradeDetailNo;

    /** 성능점검번호 */
    private Long carCheckoutNo;

    /** 미션 */
    private String carMission;

    /** 연료 */
    private String carFuel;

    /** 주행거리 */
    private Long carUseKm;

    /** 색상 */
    private String carColor;

    /** 가격 */
    private Long carAmountSale;

    /** 트럭톤수 */
    private Long carTruckTon;

    /** 상세설명 */
    private String carContent;

    /** 성능정보 URL */
    private String checkouturl;

    /** 지역 */
    private String sido;

    /** 시/군/구 */
    private String city;

    /** 이미지 정보 */
    private String imageInfo;

    /** 매물 생성 수정 일시 */
    private String lastUpdateDate;

    /** 상태 (1: 신규, 2: 기존, 3: 중복, 4: 삭제) */
    private Integer state;

    /** 등록 여부 */
    private YN listedYn;

}
