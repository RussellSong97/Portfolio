package com.cuv.admin.domain.inquirydetail.dto;

import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "InquiryDetailListDto",
        description = "상담 상세 테이블 목록 출력"
)
public class InquiryDetailListDto {

    private Long id;

    private String realName;

    @Convert(converter = MemberRole.MemberRoleConverter.class)
    private MemberRole role;

    @Convert(converter = ConsultationStatus.ConsultationStatusConverter.class)
    private ConsultationStatus consultationStatus;

    private LocalDateTime createdAt;

    private String content;

}
