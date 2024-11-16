package com.cuv.domain.boardnotice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "BoardNoticeSearchDto",
        description = "공지사항 검색조건"
)
public class BoardNoticeSearchDto {

    private String s;

    private String page;
    private String size;
}
