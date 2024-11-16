package com.cuv.domain.boardguide.dto;

import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.boardguide.enumset.BoardGuideType;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardGuideListDto {

    private Long id;

    @Convert(converter = BoardGuideType.BoardGuideTypeConverter.class)
    private BoardGuideType boardGuideType;

    private String title;

    private String content;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto attachment;


}
