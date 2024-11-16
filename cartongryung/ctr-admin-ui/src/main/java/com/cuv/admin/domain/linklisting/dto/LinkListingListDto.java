package com.cuv.admin.domain.linklisting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "LinkListingListDto",
        description = "카매니저 매물 연동 테이블 목록 출력"
)
public class LinkListingListDto {

    private Long id;

    private String shopName;

    private String userName;

    private String userHp;

    private String carPlateNumber;

    private String carFrameNo;

    private String carRegYear;

    private String brandNm;

    private String repCarClassNm;

    private String carClassNm;

    private String carGradeNm;

    private Long carUseKm;

    private Long carAmountSale;

    private LocalDateTime createdAt;

    private Boolean isCompleted;
}
