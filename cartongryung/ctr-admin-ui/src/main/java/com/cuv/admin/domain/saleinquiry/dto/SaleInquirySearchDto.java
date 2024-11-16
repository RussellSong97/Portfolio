package com.cuv.admin.domain.saleinquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "SaleInquirySearchDto",
        description = "판매 문의 검색 조건"
)
public class SaleInquirySearchDto {

    private List<String> status;
    private String field;
    private String s;
    private Long dealer;
    private String carNumber;

    private String sort;
    private String order;

    private String page;
    private String size;
}
