package com.cuv.domain.boardnotice.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(
        name = "BoardNoticeDetailDto",
        description = "공지사항 테이블 상세 출력"
)
public class BoardNoticeDetailDto {

    private Long id;

    private YN noticeYn;

    private String title;

    private Boolean newActive;

    private LocalDateTime createdAt;

    private Long hits;

    private String content;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachment;

}
