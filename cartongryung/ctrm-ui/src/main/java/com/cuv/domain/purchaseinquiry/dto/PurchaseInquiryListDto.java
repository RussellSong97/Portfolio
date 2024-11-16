package com.cuv.domain.purchaseinquiry.dto;

import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(
        name = "PurchaseInquiryListDto",
        description = "구매 문의 테이블 목록 출력"
)
public class PurchaseInquiryListDto {

    // 상담
    @Convert(converter = ConsultationStatus.ConsultationStatusConverter.class)
    private ConsultationStatus consultationStatus;

    @Convert(converter = MemberRole.MemberRoleConverter.class)
    private MemberRole inquiryDetailRole;

    private String inquiryDetailWriter;

    private LocalDateTime inquiryDetailCreatedAt;

    private LocalDateTime createdAt;

    // 방문 예약
    private Long productId;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> carImageUrl;

    private String carPlateNumber;

    private String makerName;

    private String detailModelName;

    private String detailGradeName;

    private String carRegYear;

    private Long carUseKm;

    private String carFuel;

    private String city;

    private LocalDateTime visitReservationAt;


}
