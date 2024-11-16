package com.cuv.domain.linklisting.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "link_listing")
public class LinkListing  extends BaseEntity {

    /** 매물연동 (카매니저) 테이블 시퀀스 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="car_no")
    private Long id;

    /** 제시번호 */
    @Column(name="car_jesi_no")
    private Long carJesiNo;

    /** 상사 이름 */
    @Column(name="shop_name")
    private String shopName;

    /** 딜러 명 */
    @Column(name = "user_name")
    private String userName;

    /** 딜러 휴대폰 번호 */
    @Column(name = "user_ho")
    private String userHp;

    /** 차량 번호 */
    @Column(name = "car_plate_number")
    private String carPlateNumber;

    /** 차량 이름 */
    @Column(name = "car_name")
    private String carName;

    /** 연식 */
    @Column(name = "car_reg_year")
    private String carRegYear;

    /** 차대 번호 */
    @Column(name = "car_frame_no")
    private String carFrameNo;

    /** 차량 최초 등록일 */
    @Column(name = "car_reg_day")
    private String carRegDay;

    /** 제조사 번호 */
    @Column(name = "car_maker_no")
    private Long carMakerNo;

    /** 모델 번호 */
    @Column(name = "car_model_no")
    private Long carModelNo;

    /** 등급 번호 */
    @Column(name = "car_grade_no")
    private Long carGradeNo;

    /** 상세 등급 번호 */
    @Column(name = "car_grade_detail_no")
    private Long carGradeDetailNo;

    /** 상세 모델 번호 */
    @Column(name = "car_model_detail_no")
    private Long carModelDetailNo;

    /** 성능 점검 번호 */
    @Column(name= "car_checkout_no")
    private Long carCheckoutNo;

    /** 미션 */
    @Column(name = "car_mission")
    private String carMission;

    /** 연료 */
    @Column(name = "car_fuel")
    private String carFuel;

    /** 주행 거리 */
    @Column(name = "car_use_km")
    private Long carUseKm;

    /** 색상 */
    @Column(name="car_color")
    private String carColor;

    /** 가격 */
    @Column(name = "car_amount_sale")
    private Long carAmountSale;

    /** 트럭톤 수 */
    @Column(name = "car_truck_ton")
    private Long carTruckTon;

    /** 상세 설명 */
    @Column(name = "car_content")
    private String carContent;

    /** 성능 정보 URL */
    @Column(name = "checkouturl")
    private String checkouturl;

    /** 시/군/구 */
    @Column(name = "sido")
    private String sido;

    /** 지역 */
    @Column(name = "city")
    private String city;

    /** 이미지 정보 */
    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> imageInfo;

    /** 매물 생성 수정 일시 */
    @Column(name = "last_update_date")
    private String lastUpdateDate;

    /** 상태 (1:신규, 2:기존, 3:중복, 4:삭제) */
    @Column(name = "state")
    private Long state;

    /** 등록 여부 */
    @Column(name="listed_yn")
    private YN listedYn;
}
