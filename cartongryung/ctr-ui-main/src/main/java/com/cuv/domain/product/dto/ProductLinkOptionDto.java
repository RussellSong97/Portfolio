package com.cuv.domain.product.dto;

import com.cuv.common.YN;
import lombok.Getter;

@Getter
public class ProductLinkOptionDto {
    private Long carGradeNbr;
    private Integer optType;
    private YN optPickType;
    private String optCtgry;
    private String optNm;
    private String pickOptPrice;
}
