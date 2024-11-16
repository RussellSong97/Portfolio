package com.cuv.domain.linklisting.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class LinkListingListDto {
    private Long id;
    private String carFuel;
    private Long productCount;
    private String sido;
    private String city;
    private String carFrameNo;
    private String carMission;
    private Long count;
    private List<String> carFrameNoList;
    private List<String> carSidoList;
    private List<String> carFuelList;
}
