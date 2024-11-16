package com.cuv.domain.boardnotice.dto;

import com.cuv.common.YN;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardNoticeListDto {

    private Long id;

    private YN noticeYn;

    private String title;

    private Boolean newActive;

    private LocalDateTime createdAt;

    private Long hits;

}
