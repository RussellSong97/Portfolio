package com.cuv.admin.domain.purchaseinquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "PurchaseInquirySearchDto",
        description = "구매 문의 검색 조건"
)
public class PurchaseInquirySearchDto {

    private String type;

    private List<String> status;
    private String field;
    private String s;
    private Long dealer;
    private Long counselor;
    private String sdate;
    private String edate;

    private String sort;
    private String order;

    private String page;
    private String size;
}
