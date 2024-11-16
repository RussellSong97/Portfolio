package com.cuv.admin.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(
        name = "MemberPurchaseInquiryListDto",
        description = "회원 상세 구매 목록 출력"
)
public class MemberPurchaseInquiryListDto {
    private Long id;
    private String inquiryNumber;
    private String carPlateNumber;
    private LocalDateTime createdAt;
}
