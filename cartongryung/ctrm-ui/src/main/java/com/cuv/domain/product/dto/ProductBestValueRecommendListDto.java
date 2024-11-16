package com.cuv.domain.product.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.product.enumset.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(
        name = "ProductBestValueRecommendListDto",
        description = "메인 - 가성비 추천 차량 목록 출력"
)
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

    private static final String TARGET_SHOP_NAME = "주식회사 디에스 오토";
    private static final Long DISCOUNT_AMOUNT = 1_000_000L;

    public String getFormattedCarAmountSale() {
        Long discountedAmount = (TARGET_SHOP_NAME.equals(this.shopName))
                ? (carAmountSale != null ? carAmountSale - DISCOUNT_AMOUNT : null)
                : carAmountSale;

        // 포맷팅 처리
        if (discountedAmount != null) {
            return String.format("%,d만원", discountedAmount / 10000);
        }
        return "N/A";
    }

    public String getFormattedCarComma() {
        if (carAmountSale != null) {
            return String.format("%,d만원", carAmountSale / 10000);
        } else {
            return "N/A";
        }
    }
}
