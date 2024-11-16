package com.cuv.domain.quickinquiry.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 간편 상담 기록
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "quick_inquiry")
public class QuickInquiry extends BaseEntity {

    /** 간편 상담 기록 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quick_inquiry_id")
    private Long id;

    /** 간편 상담 고객명 */
    private String costumerName;

    /** 간편 상담 고객 전화번호 */
    private String costumerNumber;

    /** 간편 상담 고객 문의란 */
    private String costumerAsk;

    /** 간편 상담 고객 진입 경로 */
    private String entryType;

    /** 간편 상담 고객 개인정보 수집 동의 */
    private YN agreeTermYn;

    /** 상담 상태 코드 {COS: 상담접수완료 | 방문예약완료 | 차량상담완료 | 결제준비중 | 승인결과안내 | 결제완료 | 인수완료 | 승인불가 | 서류준비중
     * | 거래완료 | 판매취소| 간편상담접수(11) | 간편상담완료(12)} */
    @Convert(converter = ConsultationStatus.ConsultationStatusConverter.class)
    @Column(name = "status_code")
    private ConsultationStatus consultationStatus;

}
