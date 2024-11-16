package com.cuv.domain.linklisting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Schema(
        name = "LinkListingListDto",
        description = "상품 옵션,지역,색상,연료 검색 조건 출력"
)
public class LinkListingListDto {
    private Long id;
    private String carFuel;
    private Long productCount;
    private String sido;
    private String city;
    private String carFrameNo;
    private String carMission;
    private Long count;
    private String carColor;
    private List<String> carFrameNoList;
    private List<String> carSidoList;
    private List<String> carFuelList;
}
