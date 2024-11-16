package com.cuv.domain.product.dto;

import com.cuv.common.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(
        name = "ProductLinkOptionDto",
        description = "상품 옵션 정보 출력"
)
public class ProductLinkOptionDto {
    private Long carGradeNbr;
    private Integer optType;
    private YN optPickType;
    private String optCtgry;
    private String optNm;
    private String pickOptPrice;
}
