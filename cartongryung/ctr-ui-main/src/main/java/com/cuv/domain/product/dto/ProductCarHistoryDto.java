package com.cuv.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
