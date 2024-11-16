package com.cuv.domain.linkinfo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "LinkInfoDetailDto",
        description = "카이즈유 연동 차량 정보 조회 테이블 상세 출력"
)
public class LinkInfoDetailDto {

    private String vhrno;

    private String brandNm;

    private String carClassNm;

    private String frstRegistDe;

    private Long trvlDstnc;

    private String tireSizeFront;

    private String ownerName;

    private Long linkInfoId;

}
