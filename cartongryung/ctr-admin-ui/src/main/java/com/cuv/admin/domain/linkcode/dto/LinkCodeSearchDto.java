package com.cuv.admin.domain.linkcode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "LinkCodeSearchDto",
        description = "카이즈유 연동 코드 검색 조건"
)
public class LinkCodeSearchDto {

    private Long parentLinkNbrId;

    private Integer depth;

}
