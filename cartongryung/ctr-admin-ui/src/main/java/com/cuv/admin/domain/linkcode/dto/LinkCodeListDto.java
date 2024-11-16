package com.cuv.admin.domain.linkcode.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "LinkCodeListDto",
        description = "카이즈유 연동 코드 테이블 목록 출력"
)
public class LinkCodeListDto {

    private Long id;

    private Long parentLinkNbrId;

    private String linkDataNm;

    private Integer depth;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto attachment;

    private YN viewYn;

}
