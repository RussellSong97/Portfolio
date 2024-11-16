package com.cuv.domain.saleinquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(
        name = "SaleInquirySaveDto",
        description = "판매 문의 테이블 저장 정보"
)
public class SaleInquirySaveDto {

    private String carPlateNumber;

    private String ownerName;

    private String jwtToken;

    private List<String> fileUUIDs = new ArrayList<>();

    private String agreeUseYn;

}
