package com.cuv.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "ProductTotalSearchDto",
        description = "차량 검색 정보 출력"
)
public class ProductTotalSearchDto {
    private Long maker;
    private Long model;
    private Long detailModel;

    private String mainKeyword;
    private Long categoryId;
    private String page;
    private String size;

    private String category1;
    private Long category2;
    private Integer depth;
    private String engName;
    private List<Long> category3;
    private List<Long> optionList;
    private List<String> sidoList;
    private List<String> selectedFuels;
    private List<String> selectedColors;
    private String orderBy;
    private String carPlateNumber;
    private String startYear;
    private String endYear;
    private Long startPrice;
    private Long endPrice;
    private Long startKm;
    private Long endKm;
    private String keyword;
    //    private int page;
//    private int size;
    private String pcKeyword;
}
