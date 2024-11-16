package com.cuv.domain.saleinquiry.dto;

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
        name = "SaleInquiryListDto",
        description = "판매 문의 테이블 상세 출력"
)
public class SaleInquiryDetailDto {

    private Long id;

    private String carPlateNumber;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachments;

    private LocalDateTime createdAt;

}
