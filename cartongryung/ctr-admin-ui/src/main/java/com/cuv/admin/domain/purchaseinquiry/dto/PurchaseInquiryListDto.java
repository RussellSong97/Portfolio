package com.cuv.admin.domain.purchaseinquiry.dto;

import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationType;
import com.cuv.admin.domain.purchaseinquiry.enumset.InquiryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "PurchaseInquiryListDto",
        description = "구매 상담 테이블 목록 출력"
)
public class PurchaseInquiryListDto {

    // 연락처 없는 문의
    private Long id;

    private String carPlateNumber;

    @Convert(converter = ConsultationType.ConsultationTypeConverter.class)
    private ConsultationType consultationTypeCode;

    private String connectionIp;

    private LocalDateTime createdAt;

    // 방문 예약
    private Long memberId;

    private String realName;

    private String mobileNumber;

    private String email;

    private String memberRealName;

    private String memberMobileNumber;

    private String memberEmail;

    private LocalDateTime visitReservationAt;

    private String userType; // 회원 구분 필드 추가

    // 연락 가능 문의
    private Long wishCount;

    private String dealer;

    private Long dealerCount;

    @Convert(converter = InquiryType.InquiryTypeConverter.class)
    private InquiryType inquiryTypeCode;

    @Convert(converter = ConsultationStatus.ConsultationStatusConverter.class)
    private ConsultationStatus consultationStatus;

    private String inquiryNumber;

    private String inquiryDetailWriter;

    private String inquiryDetailContent;

    // 비회원 휴대폰 번호 변환
    public String getMobileNumber() {
        if (mobileNumber != null && mobileNumber.length() == 11) {
            return mobileNumber.substring(0, 3) + "-" + mobileNumber.substring(3, 7) + "-" + mobileNumber.substring(7, 11);
        }
        return mobileNumber;
    }

    // 회원 휴대폰 번호 변환
    public String getMemberMobileNumber() {
        if (memberMobileNumber != null && memberMobileNumber.length() == 11) {
            return memberMobileNumber.substring(0, 3) + "-" + memberMobileNumber.substring(3, 7) + "-" + memberMobileNumber.substring(7, 11);
        }
        return memberMobileNumber;
    }

}
