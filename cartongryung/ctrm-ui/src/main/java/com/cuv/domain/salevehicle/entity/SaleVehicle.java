package com.cuv.domain.salevehicle.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 판매 차량
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "sale_vehicle")
public class SaleVehicle extends BaseEntity {

    /** 판매 차량 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_vehicle_id")
    private Long id;

    /** 회원 일련번호 */
    private Long memberId;

    /** 차량 정보 조회 일련번호 */
    private Long linkInfoId;

    /** 차량 번호 */
    private String carPlateNumber;

    /** 소유주 이름 */
    private String ownerName;

    /** 결제 완료 여부 */
    private YN paymentYn;

}
