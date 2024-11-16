package com.cuv.domain.saleinquiry.dto;

import com.cuv.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SaleInquiryDetailDto {

    private Long id;

    private String vhrno;

    private String brandNm;

    private String repCarClassNm;

    private String carClassNm;

    private String carGradeNm;

    private String istdTrans;

    private String colorNm;

    private String fuel;

    private String prye;

    private Long trvlDstnc;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachments;

    private LocalDateTime createdAt;

}
