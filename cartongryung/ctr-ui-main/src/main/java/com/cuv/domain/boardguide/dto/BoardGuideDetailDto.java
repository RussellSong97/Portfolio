package com.cuv.domain.boardguide.dto;

import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.boardguide.enumset.BoardGuideType;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
