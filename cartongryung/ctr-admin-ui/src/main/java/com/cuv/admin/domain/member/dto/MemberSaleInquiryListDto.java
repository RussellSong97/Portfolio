package com.cuv.admin.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(
        name = "MemberSaleInquiryListDto",
        description = "회원 상세 판매 정보 출력"
)
public class MemberSaleInquiryListDto {
    private Long id;
    private String inquiryNumber;
    private String carPlateNumber;
    private LocalDateTime createdAt;
}
