package com.cuv.admin.domain.boardfaq.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "BoardFaqSearchDto",
        description = "자주 묻는 질문 검색 조건"
)
public class BoardFaqSearchDto {
    private String type;
    private String s;

    private String page;
    private String size;

}
