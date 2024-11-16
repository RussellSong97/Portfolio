package com.cuv.admin.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "ProductSaveDto",
        description = "상품 테이블 저장 정보"
)
public class ProductSaveDto {

    private List<Long> id;

    private Long productId;

    private String postStatus;

    private String reason;

    private String type;

    private Long purchaseInquiryId;
}
