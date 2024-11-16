package com.cuv.domain.purchaseinquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "PurchaseInquirySaveDto",
        description = "구매 문의 테이블 저장 정보"
)
public class PurchaseInquirySaveDto {

    private Long productId;

    private String calendar;

    private String day;

    private String hour;

    private String minute;

    private String realName;

    private String mobileNumber;

    private String useYn;

    private String provideYn;

    private String connectionIp;

    private String inquiryNumber;
}
