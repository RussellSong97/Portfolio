package com.cuv.domain.linkinfo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LinkInfoListDto {
    private Long id;
    private String istdTrans;
    private Long count;
    private String carPlateNumber;
    private String colorName;
    private List<String> colorList;
}
