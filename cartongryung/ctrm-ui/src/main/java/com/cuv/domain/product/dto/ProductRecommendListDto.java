package com.cuv.domain.product.dto;

import com.cuv.domain.product.enumset.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Schema(
        name = "ProductRecommendListDto",
        description = "추천 차량 목록 정보 출력"
)
public class ProductRecommendListDto {

    private Long productId;

    private String productUniqueNumber;

    private String carPlateNumber;

    private String carImageUrl;

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

    private boolean picked;

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
