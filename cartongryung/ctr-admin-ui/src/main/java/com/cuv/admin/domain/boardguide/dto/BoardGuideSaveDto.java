package com.cuv.admin.domain.boardguide.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "BoardGuideSaveDto",
        description = "중고차 가이드 저장 정보"
)
public class BoardGuideSaveDto {

    private Long id;

    private String title;

    private String boardGuideType;

    private String viewYn;

    private String content;

    private String fileUUID;

}
