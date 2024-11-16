package com.cuv.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name="ProductCarHistoryDto",
        description = "보험 이력 조회 정보 출력"
)
public class ProductCarHistoryDto {
    private String joinCode;
    private String sType;
    private String memberID;
    private String carnum;
    private String carNumType;
    private String stdDate;
    private String rType;
    private String malsoGb;
    private Long productId;

    private String redirectUrl;
}
