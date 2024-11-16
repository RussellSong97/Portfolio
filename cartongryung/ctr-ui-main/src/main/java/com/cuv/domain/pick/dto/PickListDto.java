package com.cuv.domain.pick.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.product.enumset.PostStatus;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
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

    private YN recommendYn;

    public String getFormattedCarAmountSale() {
        if (carAmountSale != null) {
            return String.format("%,d만원", carAmountSale / 10000);
        } else {
            return "N/A";
        }
    }

}
