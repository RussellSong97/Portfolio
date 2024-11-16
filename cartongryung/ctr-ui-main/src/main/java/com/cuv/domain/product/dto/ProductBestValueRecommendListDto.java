package com.cuv.domain.product.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.product.enumset.PostStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductBestValueRecommendListDto {
    private Long productId;

    private String modelName;

    private String makerName;

    private String detailGradeName;

    private String shopName;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> carImageUrl;

    private String carRegYear;

    private Long carUseKm;

    private Long carAmountSale;

    /** 상품 상태 분류 코드 (PST: 대기 | 게시 | 게시 중지 | 판매 완료) */
    @Convert(converter = PostStatus.PostStatusConverter.class)
    @Column(name = "status_code")
    private PostStatus postStatus;

    private String city;

    private boolean picked;

    private YN bestValueRecommendYn;

    public String getFormattedCarAmountSale() {
        if (carAmountSale != null) {
            return String.format("%,d만원", carAmountSale / 10000);
        } else {
            return "N/A";
        }
    }
}
