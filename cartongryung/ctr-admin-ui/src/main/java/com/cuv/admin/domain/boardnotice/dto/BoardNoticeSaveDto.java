package com.cuv.admin.domain.boardnotice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "BoardNoticeSaveDto",
        description = "공지사항 테이블 저장 정보"
)
public class BoardNoticeSaveDto {
    private Long id;
    private String title;
    private String noticeYn;
    private String viewYn;
    private String content;
    private List<String> fileUUId;
}
