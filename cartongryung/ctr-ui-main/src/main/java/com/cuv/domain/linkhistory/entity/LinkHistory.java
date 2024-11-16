package com.cuv.domain.linkhistory.entity;

import com.cuv.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "link_history")
public class LinkHistory extends BaseEntity {
    /** 보험 이력 조회 테이블 시퀀스 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= " link_history_id")
    private Long id;

    /** 회원 아이디 */
    @Column(name = "member_id")
    private Long memberId;

    /** 차량 테이블 아이디 */
    @Column(name="product_id")
    private Long productId;

    /** API 전체 데이터 JSON 형태 */
    @Column(name="data")
    private String data;

    /** 일반전손사고 건수 */
    @Column(name="total_loss_count")
    private String totalLossCount;

    /** 침수전/분손사고 건수 */
    @Column(name="flood_loss_count")
    private String floodLossCount;

    /** 도난전손사고 건수 */
    @Column(name="theft_loss_count")
    private String theftLossCount;

    /** 도난전손사고일자 */
    @Column(name="theft_loss_date")
    private String theftLossDate;

    /** 용도 (1:관용 ,2:자가용 ,3:영업용,4:개인 택시) */
    @Column(name="use_type")
    private String useType;

    /** 자차 피해 사고 횟수 */
    @Column(name="self_damage_accident_count")
    private String selfDamageAccidentCount;

    /** 자차 피해 보험금액 */
    @Column(name="self_damage_insurance_sum")
    private String selfDamageInsuranceSum;

    /** 상대방차 피해 사고 횟수 */
    @Column(name="other_car_damage_accident_count")
    private String otherCarDamageAccidentCount;

    /** 상대방차 피해 보험금액 */
    @Column(name="other_car_damage_insurance_sum")
    private String otherCarDamageInsuranceSum;

    /** 소유자 변경 횟수 */
    @Column(name="owner_change_count")
    private String ownerChangeCount;

    /** 차량 주행거리 배열 */
    @Column(name = "car_distance_drive")
    private String carDistanceDrive;

    /** 수유자 변경 구분 타입 */
    @Column(name="owner_change_type")
    private String ownerChangeType;

    /** 차량 이름 */
    @Column(name ="car_name")
    private String carName;

    /** 차량 상세 이름 */
    @Column(name="car_name_detail")
    private String carNameDetail;

    /** 차량 연식 */
    @Column(name="car_year")
    private String carYear;

    /** 차량 배기량 */
    @Column(name="car_displacement")
    private String carDisplacement;

    /** 조회 일자 */
    @Column(name="standard_date")
    private String standardDate;

    /** 차체 형태 */
    @Column(name="car_body_shape")
    private String carBodyShape;

    /** 차량 옵션 */
    @Column(name="car_option")
    private String carOption;

    /** 최초 보험 가입일 */
    @Column(name="first_insurance_date")
    private String firstInsuranceDate;

    /** 사용 연료 */
    @Column(name="fuel_type")
    private String fuelType;

    /** 관용 이력 */
    @Column(name="personal_use_history")
    private String personalUseHistory;

    /** 영업 이력 */
    @Column(name="business_use_history")
    private String businessUseHistory;

    /** 대여용 이력 */
    @Column(name="rental_use_history")
    private String rentalUseHistory;

    /** 미가입 기간 */
    @Column(name = "no_join_date")
    private String noJoinDate;
}
