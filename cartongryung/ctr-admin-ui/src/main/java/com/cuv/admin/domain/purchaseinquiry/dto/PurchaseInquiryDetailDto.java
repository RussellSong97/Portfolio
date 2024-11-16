package com.cuv.admin.domain.purchaseinquiry.dto;

import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "PurchaseInquiryDetailDto",
        description = "구매 상담 테이블 상세 출력"
)
public class PurchaseInquiryDetailDto {

    private Long id;

    private String inquiryNumber;

    @Convert(converter = ConsultationStatus.ConsultationStatusConverter.class)
    private ConsultationStatus consultationStatus;

    @Convert(converter = ConsultationType.ConsultationTypeConverter.class)
    private ConsultationType consultationTypeCode;

    private Long memberId;

    private String realName;

    private String mobileNumber;

    private String email;

    private String memberRealName;

    private String memberMobileNumber;

    private String memberEmail;

    private LocalDateTime createdAt;

    private LocalDateTime visitReservationAt;

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
