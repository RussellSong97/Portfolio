package com.cuv.domain.product.dto;

import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.product.enumset.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(
        name = "ProductRecentCarListDto",
        description = "메인 - 최근 본 차량 목록 출력"
)
@Getter
@Setter
public class ProductRecentCarListDto {
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

    private static final String TARGET_SHOP_NAME = "주식회사 디에스 오토";
    private static final Long DISCOUNT_AMOUNT = 1_000_000L;

    public Long getCarAmountSale() {
        Long discountedAmount = (TARGET_SHOP_NAME.equals(this.shopName))
                ? (carAmountSale != null ? carAmountSale - DISCOUNT_AMOUNT : null)
                : carAmountSale;

        // 포맷팅 처리
        if (discountedAmount != null) {
            return discountedAmount;
        }
        return discountedAmount;
    }

    public String getCarAmount() {
        Long discountedAmount = (TARGET_SHOP_NAME.equals(this.shopName))
                ? (carAmountSale != null ? carAmountSale - DISCOUNT_AMOUNT : null)
                : carAmountSale;

        // 포맷팅 처리
        if (discountedAmount != null) {
            return String.format("%,d", discountedAmount / 10000);
        }
        return "N/A";
    }

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

    public String getFormattedMonthlyPayment(){
        Long totalCarAmount = getCarAmountSale();
        float monthlyInterestRate = (float)7 / 12 / 100;
        float upfront = (float) (totalCarAmount * 0.3);
        float payment1 = (float) ((totalCarAmount - upfront) * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, 60));
        float payment2 = (float)Math.pow(1 + monthlyInterestRate, 60) - 1;
        long monthlyPayment = Math.round(payment1/payment2);

        String format = String.format("%,d", monthlyPayment / 10000);
        return format;
    }

}
