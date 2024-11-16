package com.cuv.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(
        name = "ProductLinkSpecDto",
        description = "상품 제원 정보 출력"
)
public class ProductLinkSpecDto {
    private String specCtgry;
    private String specNm;
    private String alphanumCtgry;
    private String specUom;
    private String specValue;
}
