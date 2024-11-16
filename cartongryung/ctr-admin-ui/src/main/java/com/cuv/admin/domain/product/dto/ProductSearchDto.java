package com.cuv.admin.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "ProductSearchDto",
        description = "상품 검색 조건"
)
public class ProductSearchDto {

    private String field;
    private String s;

    private String sdate;
    private String edate;

    private Long maker;
    private Long model;
    private Long detailModel;
    private Long detailGrade;

    private List<String> status;

    private Long dealer;

    private String type;
    private String recommendYn;
    private String shopName;

    private String page;
    private String size;

    /** 상품 차량 번호 (Cyymmdd-000) */
    private String productUniqueNumber;

    /** 차량 번호 */
    private String carPlateNumber;

}
