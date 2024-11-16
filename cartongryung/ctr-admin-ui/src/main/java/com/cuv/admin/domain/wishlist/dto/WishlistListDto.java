package com.cuv.admin.domain.wishlist.dto;

import com.cuv.admin.domain.product.enumset.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "WishlistListDto",
        description = "구매 희망 차량 테이블 목록 출력"
)
public class WishlistListDto {

    private Long id;

    private Long productId;

    private String productUniqueNumber;

    private String carPlateNumber;

    @Convert(converter = PostStatus.PostStatusConverter.class)
    private PostStatus postStatus;

    private String realName;
}
