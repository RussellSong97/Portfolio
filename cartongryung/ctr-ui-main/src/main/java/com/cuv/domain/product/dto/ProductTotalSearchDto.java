package com.cuv.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductTotalSearchDto {
    private Long maker;
    private Long model;
    private Long detailModel;

    private String mainKeyword;
    private Long categoryId;
}
