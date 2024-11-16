package com.cuv.domain.boardguide.dto;

import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.boardguide.enumset.BoardGuideType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "BoardGuideDetailDto",
        description = "중고차 가이드 테이블 상세 출력"
)
public class BoardGuideDetailDto {

    private Long id;

    @Convert(converter = BoardGuideType.BoardGuideTypeConverter.class)
    private BoardGuideType boardGuideType;

    private String title;

    private LocalDateTime createdAt;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto attachment;

    private String content;

}
