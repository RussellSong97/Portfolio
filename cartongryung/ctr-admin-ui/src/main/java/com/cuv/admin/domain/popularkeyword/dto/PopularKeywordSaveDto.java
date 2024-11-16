package com.cuv.admin.domain.popularkeyword.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(
        name = "PopularKeywordSaveDto",
        description = "인기 검색어 테이블 저장 정보"
)
public class PopularKeywordSaveDto {

    private Long id;

    private Long makerCodeId;

    private Long modelCodeId;

    private Long detailModelCodeId;

}
