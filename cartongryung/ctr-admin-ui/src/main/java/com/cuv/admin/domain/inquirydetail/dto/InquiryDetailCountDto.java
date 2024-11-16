package com.cuv.admin.domain.inquirydetail.dto;

import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "InquiryDetailCountDto",
        description = "메인 > 상담 상세 테이블 개수 출력"
)
public class InquiryDetailCountDto {

    @Convert(converter = ConsultationStatus.ConsultationStatusConverter.class)
    private ConsultationStatus consultationStatus;

    private Long statusCount;

}
