package com.cuv.domain.saleinquiry.dto;

import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationStatus;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SaleInquiryListDto {

    // 내 차 팔기 > 내 차 목록
    private Long id;

    private String vhrno;

    private String brandNm;

    private String carClassNm;

    private String carGradeNm;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachments;

    private LocalDateTime createdAt;

    // 더보기 > 방문 > 내 차 팔 때
    @Convert(converter = ConsultationStatus.ConsultationStatusConverter.class)
    private ConsultationStatus consultationStatus;

    private String realName;

    private String mobileNumber;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto profileImage;

    private LocalDateTime visitReservationAt;

    public String getMobileNumber() {
        if (mobileNumber != null && mobileNumber.length() == 11) {
            return mobileNumber.substring(0, 3) + "-" + mobileNumber.substring(3, 7) + "-" + mobileNumber.substring(7, 11);
        }
        return mobileNumber;
    }

}
