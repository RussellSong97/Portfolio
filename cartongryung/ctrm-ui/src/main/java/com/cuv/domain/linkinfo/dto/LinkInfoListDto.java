package com.cuv.domain.linkinfo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(
        name = "LinkInfoListDto",
        description = "상품 제원정보 출력"
)
public class LinkInfoListDto {
    private Long id;
    private String istdTrans;
    private Long count;
    private String carPlateNumber;
    private String colorName;
    private List<String> colorList;
}
