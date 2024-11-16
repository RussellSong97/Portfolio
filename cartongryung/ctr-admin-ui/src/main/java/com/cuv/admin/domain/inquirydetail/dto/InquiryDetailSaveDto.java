package com.cuv.admin.domain.inquirydetail.dto;

import com.cuv.admin.common.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "InquiryDetailSaveDto",
        description = "상담 상세 테이블 저장 정보"
)
public class InquiryDetailSaveDto {

    private Long inquiryId;

    private Long memberId;

    private String tradeTypeCode;

    private String consultationStatus;

    private String content;

    private String mobileNumber;

    private String realName;

    private YN agreeNoticeYn;
}
