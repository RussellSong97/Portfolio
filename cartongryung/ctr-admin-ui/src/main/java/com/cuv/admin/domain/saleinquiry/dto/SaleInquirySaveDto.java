package com.cuv.admin.domain.saleinquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "SaleInquirySaveDto",
        description = "판매 문의 테이블 저장 정보"
)
public class SaleInquirySaveDto {

    private Long id;

    private Long memberDealerId;

    private String date;

    private String hour;

    private String minute;

}
