package com.cuv.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpecGroupDto {
    private String specCtgry;
    private List<ProductLinkSpecDto> specList;
    private List<ProductLinkOptionDto> optionList;
}
