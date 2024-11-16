package com.cuv.admin.domain.linklisting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "LinkListingSearchDto",
        description = "카매니저 매물 연동 검색 조건"
)
public class LinkListingSearchDto {

    private String field;
    private String s;

    private String sdate;
    private String edate;

    private Long maker;
    private Long model;
    private Long detailModel;
    private Long detailGrade;

    private String isCompleted;

    private String page;
    private String size;
}
