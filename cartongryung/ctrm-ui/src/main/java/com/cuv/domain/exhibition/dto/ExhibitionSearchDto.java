package com.cuv.domain.exhibition.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "ExhibitionSearchDto",
        description = "전시(이벤트, 팝업, 상단 띠 배너, 메인 상단 배너, 메인 서브 배너, 로그인 배너) 검색 조건"
)
public class ExhibitionSearchDto {

    private String status;

}
