package com.cuv.admin.domain.exhibition.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "ExhibitionSaveDto",
        description = "전시 테이블(이벤트, 팝업, 상단 띠 배너, 메인 상단 배너, 메인 서브 배너, 로그인 배너) 저장 정보"
)
public class ExhibitionSaveDto {

    private Long id;

    private String exhibitionType;

    private String title;

    private String content;

    private String pcFileUUID;

    private String moFileUUID;

    private String exhibitionStartDate;

    private String exhibitionEndDate;

    private String url;

    private String linkType;

    private String backgroundColorCode;

    private String viewYn;

    private int position;
}
