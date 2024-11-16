package com.cuv.domain.boardreview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "BoardReviewSearchDto",
        description = "이용후기 검색조건"
)
public class BoardReviewSearchDto {
    private String page;
    private String size;
}
