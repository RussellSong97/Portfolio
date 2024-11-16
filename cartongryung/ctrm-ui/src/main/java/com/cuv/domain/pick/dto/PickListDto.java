package com.cuv.domain.pick.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.product.enumset.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "PickListDto",
        description = "픽 테이블 목록 출력"
)

public class PickListDto {
    /**
     * pick
     */
    private Long pickId;

    private Long memberId;

    private Long productId;

    private YN readYn;

    /**
     * productId로 join해서 가져올 것
     */
    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> carImageUrl;

    private String shopName;

    private String makerName;

    private String modelName;

    private String detailGradeName;

    private String carFuel;

    private String carRegYear;

    private Long carUseKm;

    private Long carAmountSale;

    @Convert(converter = PostStatus.PostStatusConverter.class)
    private PostStatus postStatus;

    private String city;

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

}
