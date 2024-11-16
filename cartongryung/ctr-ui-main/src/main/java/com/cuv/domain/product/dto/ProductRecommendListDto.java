package com.cuv.domain.product.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.product.enumset.PostStatus;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class ProductRecommendListDto {
    private Long productId;

    private String productUniqueNumber;

    private String carPlateNumber;

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

    private YN recommendYn;

    private boolean picked;

    public String getFormattedCarAmountSale() {
        if (carAmountSale != null) {
            return String.format("%,d만원", carAmountSale / 10000);
        } else {
            return "N/A";
        }
    }
}
