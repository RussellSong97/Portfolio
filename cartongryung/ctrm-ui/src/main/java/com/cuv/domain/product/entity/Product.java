package com.cuv.domain.product.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.product.enumset.CarSize;
import com.cuv.domain.product.enumset.ExteriorShape;
import com.cuv.domain.product.enumset.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 상품
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "product")
public class Product extends BaseEntity {

    /** 상품 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    /** 딜러 일련번호 */
    private Long memberDealerId;

    /** 차대 번호 */
    private String vehicleIdentificationNumber;

    /** 상품 차량 번호 (Cyymmdd-000) */
    private String productUniqueNumber;

    /** 차량 번호 */
    private String carPlateNumber;

    /** 차량 이미지 URL */
    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> carImageUrl;

    /** 상사 이름 */
    private String shopName;

    /** 제조사 번호 */
    private Long makerNumber;

    /** 모델 번호 */
    private Long modelNumber;

    /** 상세 모델 번호 */
    private Long detailModelNumber;

    /** 상세 등급 번호 */
    private Long detailGradeNumber;

    /** 외형 (EXT : BUS | CONVERTIBLE | COUPE | HATCHBACK | LIMOUSINE | MINIBUS | PICKUPTRUCK | RV | SEDAN | SUV | TRUCK | WAGON) */
    @Convert(converter = ExteriorShape.ExteriorShapeConverter.class)
    private ExteriorShape extShape;

    /** 차급 (CAS : A: 경형 | B:소형 | C:준중형 | D:중형 | E:준대형 | F:대형 | OTHER:기타) */
    @Convert(converter = CarSize.CarSizeConverter.class)
    private CarSize carSize;

    /** 차량 최초 등록일 */
    private String carRegDay;

    /** 연식 */
    private String carRegYear;

    /** 미션 */
    private String carMission;

    /** 연료 */
    private String carFuel;

    /** 색상 */
    private String carColor;

    /** 주행거리 */
    private Long carUseKm;

    /** 지역 */
    private String city;

    /** 가격 */
    private Long carAmountSale;

    /** 성능점검기록부 링크 URL */
    private String performanceInspectionUrl;

    /** 비고 */
    private String content;

    /** 상품 상태 분류 코드 (PST: 대기 | 게시 | 게시 중지 | 판매 완료) */
    @Convert(converter = PostStatus.PostStatusConverter.class)
    @Column(name = "status_code")
    private PostStatus postStatus;

    /** 게시 중지 사유 */
    private String postStopReason;

    /** 카통령 추천 차량 여부 */
    private YN cartongryeongRecommendYn;

    /** 가성비 추천 차량 여부 */
    private YN bestValueRecommendYn;

    /** 조회수 */
    private Integer hits;

    /** 판매완료 일시 */
    private LocalDateTime soldOutAt;

    /** 조회수 증가 */
    public void addHits() {
        ++this.hits;
    }

}
