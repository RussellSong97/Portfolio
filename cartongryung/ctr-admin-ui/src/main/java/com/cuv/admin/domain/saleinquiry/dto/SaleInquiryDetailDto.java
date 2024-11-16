package com.cuv.admin.domain.saleinquiry.dto;

import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "SaleInquiryDetailDto",
        description = "판매 문의 테이블 상세 출력"
)
public class SaleInquiryDetailDto {

    private Long id;

    private String inquiryNumber;

    private String carPlateNumber;

    @Convert(converter = ConsultationStatus.ConsultationStatusConverter.class)
    private ConsultationStatus consultationStatus;

    private Long memberId;

    private String realName;

    private String mobileNumber;

    private String email;

    private LocalDateTime createdAt;

    private Long memberDealerId;

    private String dealer;

    private LocalDateTime visitReservationAt;

    // 휴대폰 번호 변환
    public String getMobileNumber() {
        if (mobileNumber != null && mobileNumber.length() == 11) {
            return mobileNumber.substring(0, 3) + "-" + mobileNumber.substring(3, 7) + "-" + mobileNumber.substring(7, 11);
        }
        return mobileNumber;
    }

}
