package com.cuv.admin.domain.boardnotice.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
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
    private String title;
    private String content;
    private YN noticeYn;
    private YN viewYn;
    private String createdId;
    private LocalDateTime createdAt;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachment;
}
