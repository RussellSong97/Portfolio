package com.cuv.admin.domain.linkcode.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(
        name = "LinkCodeDetailDto",
        description = "카이즈유 연동 코드 테이블 상세 출력"
)
public class LinkCodeDetailDto {

    private Long id;

    private String linkDataNm;

    private Integer depth;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private AttachmentDto attachment;

    private LocalDate afterServiceDate;

    private YN viewYn;

}
