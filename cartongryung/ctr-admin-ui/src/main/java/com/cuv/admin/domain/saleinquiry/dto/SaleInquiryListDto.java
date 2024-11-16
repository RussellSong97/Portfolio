package com.cuv.admin.domain.saleinquiry.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(
        name = "SaleInquiryListDto",
        description = "판매 문의 테이블 목록 출력"
)
public class SaleInquiryListDto {

    private Long id;

    private Long memberId;

    private Long saleVehicleId;

    private String inquiryNumber;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachments;

    private String carPlateNumber;

    @Convert(converter = ConsultationStatus.ConsultationStatusConverter.class)
    private ConsultationStatus consultationStatus;

    private String realName;

    private String email;

    private String mobileNumber;

    private LocalDateTime visitReservationAt;

    private LocalDateTime createdAt;

    private YN paymentYn;

    private String dealer;

    private String inquiryDetailWriter;

    private String inquiryDetailContent;

    // 휴대폰 번호 변환
    public String getMobileNumber() {
        if (mobileNumber != null && mobileNumber.length() == 11) {
            return mobileNumber.substring(0, 3) + "-" + mobileNumber.substring(3, 7) + "-" + mobileNumber.substring(7, 11);
        }
        return mobileNumber;
    }

}
