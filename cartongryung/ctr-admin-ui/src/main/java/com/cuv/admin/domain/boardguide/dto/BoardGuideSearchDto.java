package com.cuv.admin.domain.boardguide.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "BoardGuideSearchDto",
        description = "중고차 가이드 검색 조건"
)
public class BoardGuideSearchDto {

    private String boardGuideType;
    private String s;

    private String page;
    private String size;
}
