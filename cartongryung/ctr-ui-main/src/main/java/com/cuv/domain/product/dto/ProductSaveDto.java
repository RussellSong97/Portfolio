package com.cuv.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductSaveDto {

    private List<Long> id;

    private Long productId;

    private String postStatus;

    private String reason;

}
