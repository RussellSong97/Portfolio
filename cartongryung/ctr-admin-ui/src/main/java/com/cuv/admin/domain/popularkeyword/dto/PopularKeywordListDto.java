package com.cuv.admin.domain.popularkeyword.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(
        name = "PopularKeywordListDto",
        description = "인기 검색어 테이블 목록 출력"
)
public class PopularKeywordListDto {

    private Long id;

    private Long makerCodeId;

    private Long modelCodeId;

}
