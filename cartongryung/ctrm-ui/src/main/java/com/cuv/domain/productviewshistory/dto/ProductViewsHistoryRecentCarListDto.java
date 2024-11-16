package com.cuv.domain.productviewshistory.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(
        name = "ProductViewsHistoryMyPageRecentCarListDto",
        description = "aside - 최근 본 차량 목록 출력"
)
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
