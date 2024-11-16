package com.cuv.domain.productviewshistory.dto;

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
public class ProductViewsHistoryMyPageRecentCarListDto {
    private Long id;

    private String modelName;

    private String makerName;

    private Long productId;

    private String detailGradeName;

    private String shopName;

    private String carRegYear;

    private Long carUseKm;

    private Long carAmountSale;

    /** 상품 상태 분류 코드 (PST: 대기 | 게시 | 게시 중지 | 판매 완료) */
    @Convert(converter = PostStatus.PostStatusConverter.class)
    @Column(name = "status_code")
    private PostStatus postStatus;

    private String city;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> carImageUrl;

    private YN del_yn;

    public String getFormattedCarAmountSale() {
        if (carAmountSale != null) {
            return String.format("%,d만원", carAmountSale / 10000);
        } else {
            return "N/A";
        }
    }
}
