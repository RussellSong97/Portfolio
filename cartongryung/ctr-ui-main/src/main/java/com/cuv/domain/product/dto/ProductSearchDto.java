package com.cuv.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductSearchDto {

    private String field;
    private String s;

    private String sdate;
    private String edate;

    private Long maker;
    private Long model;
    private Long detailModel;
    private Long detailGrade;

    private List<String> status;

    private String page;
    private String size;

}
