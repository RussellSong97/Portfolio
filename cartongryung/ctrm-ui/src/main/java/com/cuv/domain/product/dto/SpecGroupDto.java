package com.cuv.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "SpecGroupDto",
        description = "제원 정보, 옵션 정보 그룹 출력"
)
public class SpecGroupDto {
    private String specCtgry;
    private List<ProductLinkSpecDto> specList;
    private List<ProductLinkOptionDto> optionList;
}
