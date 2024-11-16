package com.cuv.admin.domain.product.dto;

import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import com.cuv.admin.domain.product.enumset.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(
        name = "ProductListDto",
        description = "상품 테이블 목록 출력"
)
public class ProductListDto {

    private Long productId;

    private String productUniqueNumber;

    private String carPlateNumber;

    private String realName;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> carImageUrl;

    private String shopName;

    private String makerName;

    private String modelName;

    private String detailGradeName;

    private String carRegYear;

    private Long carUseKm;

    private Long carAmountSale;

    @Convert(converter = PostStatus.PostStatusConverter.class)
    private PostStatus postStatus;

    private String city;

    private LocalDateTime createdAt;

}
