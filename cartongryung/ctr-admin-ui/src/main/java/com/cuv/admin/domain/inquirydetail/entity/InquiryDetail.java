package com.cuv.admin.domain.inquirydetail.entity;

import com.cuv.admin.common.BaseEntity;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.admin.domain.inquirydetail.enumset.TradeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 상담 기록 상세
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "inquiry_detail")
public class InquiryDetail extends BaseEntity {

    /** 상담 기록 상세 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_detail_id")
    private Long id;

    /** 문의 일련번호 */
    private Long inquiryId;

    /** 관리자 회원 일련번호 */
    private Long memberAdminId;

    /** 매매 구분 코드 (TRD: 구매 | 판매) */
    @Convert(converter = TradeType.TradeTypeConverter.class)
    private TradeType tradeTypeCode;

    /** 상담 상태 코드 (COS: 상담접수완료 | 방문예약완료 | 차량상담완료 | 결제준비중 | 승인결과안내 | 결제완료 | 인수완료 | 승인불가 | 서류준비중
     * | 거래완료 | 판매취소) */
    @Convert(converter = ConsultationStatus.ConsultationStatusConverter.class)
    @Column(name = "status_code")
    private ConsultationStatus consultationStatus;

    /** 내용 */
    private String content;

}
