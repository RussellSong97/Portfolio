package com.cuv.admin.domain.purchaseinquiry.dto;

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

    private Long id;

    private List<Long> productId;

    private String saveConsultationTypeCode;

    private String date;

    private String hour;

    private String minute;

}
