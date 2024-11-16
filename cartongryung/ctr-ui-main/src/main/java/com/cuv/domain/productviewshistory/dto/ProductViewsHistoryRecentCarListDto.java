package com.cuv.domain.productviewshistory.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductViewsHistoryRecentCarListDto {
    private Long id;

    private String modelName;

    private String makerName;

    private Long productId;

    private String detailGradeName;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> carImageUrl;

    private YN del_yn;
}
