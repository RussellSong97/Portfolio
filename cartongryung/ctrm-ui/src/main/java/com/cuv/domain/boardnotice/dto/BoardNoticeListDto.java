package com.cuv.domain.boardnotice.dto;

import com.cuv.common.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "BoardNoticeListDto",
        description = "공지사항 테이블 목록 출력"
)
public class BoardNoticeListDto {

    private Long id;

    private YN noticeYn;

    private String title;

    private Boolean newActive;

    private LocalDateTime createdAt;

    private Long hits;

}
