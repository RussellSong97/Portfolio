package com.cuv.domain.boardfaq.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "BoardFaqListDto",
        description = "자주 묻는 질문 테이블 목록 출력"
)
public class BoardFaqListDto {

    private String title;

    private String content;
}
