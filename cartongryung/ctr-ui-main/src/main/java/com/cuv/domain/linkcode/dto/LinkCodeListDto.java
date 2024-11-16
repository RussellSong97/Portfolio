package com.cuv.domain.linkcode.dto;

import com.cuv.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkCodeListDto {
    private Long id;
    private Long parentId;
    private String linkDataNm;
    private Integer depth;
    private String brandImageUrl;
    private Long count;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto carImage;
}

