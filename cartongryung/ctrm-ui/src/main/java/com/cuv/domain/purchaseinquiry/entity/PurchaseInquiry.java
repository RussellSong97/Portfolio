package com.cuv.domain.purchaseinquiry.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationType;
import com.cuv.domain.purchaseinquiry.enumset.InquiryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 구매 문의
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "purchase_inquiry")
public class PurchaseInquiry extends BaseEntity {

    /** 구매 문의 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_inquiry_id")
    private Long id;

    /** 회원 일련번호 */
    private Long memberId;

    /** 문의 분류 코드 (INQ: 연락 가능 문의 | 연락처 없는 문의 | 방문 예약) */
    @Convert(converter = InquiryType.InquiryTypeConverter.class)
    private InquiryType inquiryTypeCode;

    /** 상담 분류 코드 (CSL: 채팅 | 전화 | 방문 예약) */
    @Convert(converter = ConsultationType.ConsultationTypeConverter.class)
    private ConsultationType consultationTypeCode;

    /** 문의 번호 (Byymmdd-000) */
    private String inquiryNumber;

    /** 이름 (실명) */
    private String realName;

    /** 휴대전화 */
    private String mobileNumber;

    /** 이메일 */
    private String email;

    /** 접속 아이피 */
    private String connectionIp;

    /** 방문 예약 일시 */
    private LocalDateTime visitReservationAt;

}
